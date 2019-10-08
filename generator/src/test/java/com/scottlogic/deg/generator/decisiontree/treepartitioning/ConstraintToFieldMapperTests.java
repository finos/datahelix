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

package com.scottlogic.deg.generator.decisiontree.treepartitioning;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.FieldBuilder;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.profile.constraints.atomic.IsInSetConstraint;
import com.scottlogic.deg.generator.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.decisiontree.ConstraintNodeBuilder;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.generator.fieldspecs.whitelist.WeightedElement;
import com.scottlogic.deg.generator.utils.SetUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import static com.scottlogic.deg.common.profile.FieldBuilder.createField;

class ConstraintToFieldMapperTests {

    private DistributedList<Object> whitelistOf(Object element) {
        return new DistributedList<>(Collections.singletonList(new WeightedElement<>(element, 1.0F)));
    }

    @Test
    void shouldFindConstraintMappings() {
        givenFields("A");

        final AtomicConstraint constraint = new IsInSetConstraint(createField("A"), whitelistOf("test-value"));
        givenConstraints(constraint);
        givenFields("A");

        expectMapping(constraint, "A");
    }

    @Test
    void shouldFindRootDecisionNodeMapping() {
        givenFields("B");

        final AtomicConstraint constraint = new IsInSetConstraint(createField("B"), whitelistOf("test-value"));
        final DecisionNode decision = new DecisionNode(
            new ConstraintNodeBuilder().addAtomicConstraints(constraint).build());

        givenDecisions(decision);

        expectMapping(decision, "B");
    }

    @Test
    void shouldCreateCorrectNumberOfMappings() {
        givenFields("A", "B", "C");

        final AtomicConstraint constraintA = new IsInSetConstraint(createField("A"), whitelistOf("test-value"));
        final AtomicConstraint constraintB = new IsInSetConstraint(createField("B"), whitelistOf("test-value"));
        final AtomicConstraint constraintC = new IsInSetConstraint(createField("C"), whitelistOf("test-value"));

        givenConstraints(constraintA, constraintB, constraintC);

        expectMappingCount(3);
    }

    @Test
    void shouldMapTopLevelConstraintsToNestedFields() {
        givenFields("A", "B", "C", "D", "E", "F");

        final AtomicConstraint constraintA = new IsInSetConstraint(createField("A"), whitelistOf("test-value"));
        final AtomicConstraint constraintB = new IsInSetConstraint(createField("B"), whitelistOf("test-value"));
        final AtomicConstraint constraintC = new IsInSetConstraint(createField("C"), whitelistOf("test-value"));
        final AtomicConstraint constraintD = new IsInSetConstraint(createField("D"), whitelistOf("test-value"));
        final AtomicConstraint constraintE = new IsInSetConstraint(createField("E"), whitelistOf("test-value"));
        final AtomicConstraint constraintF = new IsInSetConstraint(createField("F"), whitelistOf("test-value"));

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
        fields = new ProfileFields(Collections.emptyList());
        mappings = null;
    }

    private Set<AtomicConstraint> constraintsSet;
    private Set<DecisionNode> decisionsSet;
    private ProfileFields fields;
    private Map<RootLevelConstraint, Set<Field>> mappings;

    private void givenConstraints(AtomicConstraint... constraints) {
        constraintsSet = SetUtils.setOf(constraints);
    }

    private void givenDecisions(DecisionNode... decisions) {
        decisionsSet = SetUtils.setOf(decisions);
    }

    private void givenFields(String... fieldNames) {
        fields = new ProfileFields(
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
