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

package com.scottlogic.datahelix.generator.core.decisiontree.treepartitioning;

import com.scottlogic.datahelix.generator.common.profile.Field;
import com.scottlogic.datahelix.generator.common.profile.FieldBuilder;
import com.scottlogic.datahelix.generator.common.profile.Fields;
import com.scottlogic.datahelix.generator.core.decisiontree.ConstraintNodeBuilder;
import com.scottlogic.datahelix.generator.core.decisiontree.DecisionNode;
import com.scottlogic.datahelix.generator.core.profile.constraints.atomic.InSetConstraint;
import com.scottlogic.datahelix.generator.core.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.datahelix.generator.core.decisiontree.DecisionTree;
import com.scottlogic.datahelix.generator.common.whitelist.DistributedList;
import com.scottlogic.datahelix.generator.common.whitelist.WeightedElement;
import com.scottlogic.datahelix.generator.common.SetUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import static com.scottlogic.datahelix.generator.common.profile.FieldBuilder.createField;

class ConstraintToFieldMapperTests {

    private DistributedList<Object> whitelistOf(Object element) {
        return new DistributedList<>(Collections.singletonList(new WeightedElement<>(element, 1.0F)));
    }

    @Test
    void shouldFindConstraintMappings() {
        givenFields("A");

        final AtomicConstraint constraint = new InSetConstraint(createField("A"), whitelistOf("test-value"));
        givenConstraints(constraint);
        givenFields("A");

        expectMapping(constraint, "A");
    }

    @Test
    void shouldFindRootDecisionNodeMapping() {
        givenFields("B");

        final AtomicConstraint constraint = new InSetConstraint(createField("B"), whitelistOf("test-value"));
        final DecisionNode decision = new DecisionNode(
            new ConstraintNodeBuilder().addAtomicConstraints(constraint).build());

        givenDecisions(decision);

        expectMapping(decision, "B");
    }

    @Test
    void shouldCreateCorrectNumberOfMappings() {
        givenFields("A", "B", "C");

        final AtomicConstraint constraintA = new InSetConstraint(createField("A"), whitelistOf("test-value"));
        final AtomicConstraint constraintB = new InSetConstraint(createField("B"), whitelistOf("test-value"));
        final AtomicConstraint constraintC = new InSetConstraint(createField("C"), whitelistOf("test-value"));

        givenConstraints(constraintA, constraintB, constraintC);

        expectMappingCount(3);
    }

    @Test
    void shouldMapTopLevelConstraintsToNestedFields() {
        givenFields("A", "B", "C", "D", "E", "F");

        final AtomicConstraint constraintA = new InSetConstraint(createField("A"), whitelistOf("test-value"));
        final AtomicConstraint constraintB = new InSetConstraint(createField("B"), whitelistOf("test-value"));
        final AtomicConstraint constraintC = new InSetConstraint(createField("C"), whitelistOf("test-value"));
        final AtomicConstraint constraintD = new InSetConstraint(createField("D"), whitelistOf("test-value"));
        final AtomicConstraint constraintE = new InSetConstraint(createField("E"), whitelistOf("test-value"));
        final AtomicConstraint constraintF = new InSetConstraint(createField("F"), whitelistOf("test-value"));

        final DecisionNode decisionABC = new DecisionNode(
            new ConstraintNodeBuilder().addAtomicConstraints(Collections.emptySet()).setDecisions(SetUtils.setOf(
                new DecisionNode(new ConstraintNodeBuilder().addAtomicConstraints(constraintA).build()),
                new DecisionNode(new ConstraintNodeBuilder().addAtomicConstraints(constraintB).build()),
                new DecisionNode(new ConstraintNodeBuilder().addAtomicConstraints(constraintC).build())
            )).build()
        );

        final DecisionNode decisionDEF = new DecisionNode(
            new ConstraintNodeBuilder().addAtomicConstraints(Collections.emptySet()).setDecisions(Collections.singleton(
                new DecisionNode(
                    new ConstraintNodeBuilder().addAtomicConstraints(constraintD).build(),
                    new ConstraintNodeBuilder().addAtomicConstraints(constraintE).build(),
                    new ConstraintNodeBuilder().addAtomicConstraints(constraintF).build())
            )).build()
        );

        givenDecisions(decisionABC, decisionDEF);

        expectMapping(decisionABC, "A", "B", "C");
        expectMapping(decisionDEF, "D", "E", "F");
    }

    @BeforeEach
    void beforeEach() {
        constraintsSet = new HashSet<>();
        decisionsSet = new HashSet<>();
        fields = new Fields(Collections.emptyList());
        mappings = null;
    }

    private Set<AtomicConstraint> constraintsSet;
    private Set<DecisionNode> decisionsSet;
    private Fields fields;
    private Map<RootLevelConstraint, Set<Field>> mappings;

    private void givenConstraints(AtomicConstraint... constraints) {
        constraintsSet = SetUtils.setOf(constraints);
    }

    private void givenDecisions(DecisionNode... decisions) {
        decisionsSet = SetUtils.setOf(decisions);
    }

    private void givenFields(String... fieldNames) {
        fields = new Fields(
            Arrays.stream(fieldNames)
                .map(FieldBuilder::createField)
                .collect(Collectors.toList()));
    }

    private void getMappings() {
        mappings = new ConstraintToFieldMapper()
            .mapConstraintsToFields(new DecisionTree(
                new ConstraintNodeBuilder().addAtomicConstraints(constraintsSet).setDecisions(decisionsSet).build(),
                fields
            ));
    }

    private void expectMapping(AtomicConstraint constraint, String... fieldsAsString) {
        if (mappings == null)
            getMappings();

        final Field[] fields = Arrays.stream(fieldsAsString)
            .map(FieldBuilder::createField)
            .toArray(Field[]::new);

        Assert.assertThat(mappings.get(new RootLevelConstraint(constraint)), Matchers.hasItems(fields));
    }

    private void expectMapping(DecisionNode decision, String... fieldsAsString) {
        if (mappings == null)
            getMappings();

        final Field[] fields = Arrays.stream(fieldsAsString)
            .map(FieldBuilder::createField)
            .toArray(Field[]::new);

        Assert.assertThat(mappings.get(new RootLevelConstraint(decision)), Matchers.hasItems(fields));
    }

    private void expectMappingCount(int mappingsCount) {
        if (mappings == null)
            getMappings();

        Assert.assertThat(mappings, Matchers.aMapWithSize(mappingsCount));
    }
}
