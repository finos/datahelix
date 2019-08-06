/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.builders.TestConstraintNodeBuilder;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.ConstraintNodeBuilder;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.whitelist.FrequencyDistributedSet;
import com.scottlogic.deg.generator.generation.FieldSpecValueGenerator;
import com.scottlogic.deg.generator.generation.NoopDataGeneratorMonitor;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.generator.walker.reductive.Merged;
import com.scottlogic.deg.generator.walker.reductive.NoOpIterationVisualiser;
import com.scottlogic.deg.generator.walker.reductive.ReductiveFieldSpecBuilder;
import com.scottlogic.deg.generator.walker.reductive.ReductiveTreePruner;
import com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy.FixFieldStrategy;
import com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy.FixFieldStrategyFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.Duration.ofMillis;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

class ReductiveDecisionTreeWalkerTests {
    private ConstraintNode rootNode;
    private DecisionTree tree;
    private ReductiveFieldSpecBuilder reductiveFieldSpecBuilder;
    private ReductiveDecisionTreeWalker walker;
    private FixFieldStrategy fixFieldStrategy;
    private FixFieldStrategyFactory fixFieldStrategyFactory;
    private FieldSpecValueGenerator fieldSpecValueGenerator;
    private ReductiveTreePruner treePruner;
    private ReductiveWalkerRetryChecker retryChecker = new ReductiveWalkerRetryChecker(100);
    private Field field1 = new Field("field1");
    private Field field2 = new Field("field2");

    @BeforeEach
    public void beforeEach(){
        ProfileFields fields = new ProfileFields(Arrays.asList(field1, field2));
        rootNode = new ConstraintNodeBuilder().build();
        tree = new DecisionTree(rootNode, fields);

        reductiveFieldSpecBuilder = mock(ReductiveFieldSpecBuilder.class);
        fieldSpecValueGenerator = mock(FieldSpecValueGenerator.class);
        fixFieldStrategy = mock(FixFieldStrategy.class);
        when(fixFieldStrategy.getNextFieldToFix(any())).thenReturn(field1, field2);
        fixFieldStrategyFactory = mock(FixFieldStrategyFactory.class);
        when(fixFieldStrategyFactory.create(any())).thenReturn(fixFieldStrategy);
        treePruner = mock(ReductiveTreePruner.class);
        when(treePruner.pruneConstraintNode(eq(rootNode), any(), any())).thenReturn(Merged.of(rootNode));

        walker = new ReductiveDecisionTreeWalker(
            new NoOpIterationVisualiser(),
            reductiveFieldSpecBuilder,
            new NoopDataGeneratorMonitor(),
            treePruner,
            fieldSpecValueGenerator,
            fixFieldStrategyFactory,
            retryChecker
        );
    }

    /**
     * If no field can be fixed initially, the walker should exit early, with an empty stream of RowSpecs
     */
    @Test
    public void shouldReturnEmptyCollectionOfRowsWhenFirstFieldCannotBeFixed() {
        when(reductiveFieldSpecBuilder.getDecisionFieldSpecs(eq(rootNode), any())).thenReturn(Collections.EMPTY_SET);

        List<DataBag> result = walker.walk(tree).collect(Collectors.toList());

        verify(reductiveFieldSpecBuilder).getDecisionFieldSpecs(eq(rootNode), any());
        assertThat(result, empty());
    }

    /**
     * If a field can be fixed initially, but subsequently another one cannot be fixed then exit as early as possible
     * with an empty stream of RowSpecs
     */
    @Test
    public void shouldReturnEmptyCollectionOfRowsWhenSecondFieldCannotBeFixed() {
        DataBagValue dataBag = new DataBagValue(field1, "yes");
        FieldSpec firstFieldSpec = FieldSpec.Empty.withWhitelist(FrequencyDistributedSet.uniform(Collections.singleton("yes")))
            .withNotNull();
        when(fieldSpecValueGenerator.generate(any(Set.class))).thenReturn(Stream.of(dataBag));

        when(reductiveFieldSpecBuilder.getDecisionFieldSpecs(any(), any())).thenReturn(Collections.singleton(firstFieldSpec), Collections.emptySet());

        List<DataBag> result = walker.walk(tree).collect(Collectors.toList());

        verify(reductiveFieldSpecBuilder, times(2)).getDecisionFieldSpecs(eq(rootNode), any());
        assertThat(result, empty());
    }

    @Test
    public void walk_whereFirstFieldCannotBeFixed_throwsException() {
        ProfileFields fields = new ProfileFields(Arrays.asList(field1, field2));
        FieldSpec firstFieldSpec = FieldSpec.Empty.withNotNull();
        FieldSpec secondFieldSpec = FieldSpec.Empty.withNotNull();
        Set<FieldSpec> fieldSpecs = new HashSet<>();
        fieldSpecs.add(firstFieldSpec);
        fieldSpecs.add(secondFieldSpec);
        ConstraintNode root = TestConstraintNodeBuilder.constraintNode()
            .where(field1).isNull()
            .where(field1).isNotNull()
            .build();
        DecisionTree tree = new DecisionTree(root, fields);
        DataBagValue dataBagValue = mock(DataBagValue.class);
        when(fixFieldStrategy.getNextFieldToFix(any())).thenReturn(field1, field2);
        when(fixFieldStrategyFactory.create(any())).thenReturn(fixFieldStrategy);
        when(treePruner.pruneConstraintNode(eq(root), any(), any())).thenReturn(Merged.of(root));
        when(reductiveFieldSpecBuilder.getDecisionFieldSpecs(any(), any())).thenReturn(fieldSpecs);
        when(treePruner.pruneConstraintNode(eq(root), any(), any())).thenReturn(Merged.contradictory());

        Stream<DataBagValue> infiniteStream = Stream.iterate(dataBagValue, i -> dataBagValue);
        when(fieldSpecValueGenerator.generate(anySetOf(FieldSpec.class))).thenReturn(infiniteStream);

        assertTimeoutPreemptively(ofMillis(100), () -> {
            assertThrows(RetryLimitReachedException.class, () -> walker.walk(tree).findFirst());
        });
    }
}