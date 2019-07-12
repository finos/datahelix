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
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.common.profile.constraints.atomic.IsInSetConstraint;
import com.scottlogic.deg.common.profile.RuleInformation;
import com.scottlogic.deg.common.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.TreeConstraintNode;
import com.scottlogic.deg.generator.decisiontree.TreeDecisionNode;
import com.scottlogic.deg.generator.fieldspecs.whitelist.ElementFrequency;
import com.scottlogic.deg.generator.fieldspecs.whitelist.FrequencyWhitelist;
import com.scottlogic.deg.generator.fieldspecs.whitelist.Whitelist;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ConstraintToFieldMapperTests {

    private Whitelist<Object> whitelistOf(Object element) {
        return new FrequencyWhitelist<Object>(Collections.singleton(new ElementFrequency<>(element, 1.0F)));
    }

    @Test
    void shouldFindConstraintMappings() {
        givenFields("A");

        final AtomicConstraint constraint = new IsInSetConstraint(new Field("A"), whitelistOf("test-value"));
        givenConstraints(constraint);
        givenFields("A");

        expectMapping(constraint, "A");
    }

    @Test
    void shouldFindRootDecisionNodeMapping() {
        givenFields("B");

        final AtomicConstraint constraint = new IsInSetConstraint(new Field("B"), whitelistOf("test-value"));
        final DecisionNode decision = new TreeDecisionNode(
            new TreeConstraintNode(constraint));

        givenDecisions(decision);

        expectMapping(decision, "B");
    }

    @Test
    void shouldCreateCorrectNumberOfMappings() {
        givenFields("A", "B", "C");

        final AtomicConstraint constraintA = new IsInSetConstraint(new Field("A"), whitelistOf("test-value"));
        final AtomicConstraint constraintB = new IsInSetConstraint(new Field("B"), whitelistOf("test-value"));
        final AtomicConstraint constraintC = new IsInSetConstraint(new Field("C"), whitelistOf("test-value"));

        givenConstraints(constraintA, constraintB, constraintC);

        expectMappingCount(3);
    }

    @Test
    void shouldMapTopLevelConstraintsToNestedFields() {
        givenFields("A", "B", "C", "D", "E", "F");

        final AtomicConstraint constraintA = new IsInSetConstraint(new Field("A"), whitelistOf("test-value"));
        final AtomicConstraint constraintB = new IsInSetConstraint(new Field("B"), whitelistOf("test-value"));
        final AtomicConstraint constraintC = new IsInSetConstraint(new Field("C"), whitelistOf("test-value"));
        final AtomicConstraint constraintD = new IsInSetConstraint(new Field("D"), whitelistOf("test-value"));
        final AtomicConstraint constraintE = new IsInSetConstraint(new Field("E"), whitelistOf("test-value"));
        final AtomicConstraint constraintF = new IsInSetConstraint(new Field("F"), whitelistOf("test-value"));

        final DecisionNode decisionABC = new TreeDecisionNode(
            new TreeConstraintNode(
                Collections.emptyList(),
                Arrays.asList(
                    new TreeDecisionNode(new TreeConstraintNode(constraintA)),
                    new TreeDecisionNode(new TreeConstraintNode(constraintB)),
                    new TreeDecisionNode(new TreeConstraintNode(constraintC))
                )
            )
        );

        final DecisionNode decisionDEF = new TreeDecisionNode(
            new TreeConstraintNode(
                Collections.emptyList(),
                Collections.singletonList(
                    new TreeDecisionNode(
                        new TreeConstraintNode(constraintD),
                        new TreeConstraintNode(constraintE),
                        new TreeConstraintNode(constraintF))
                )
            )
        );

        givenDecisions(decisionABC, decisionDEF);

        expectMapping(decisionABC, "A", "B", "C");
        expectMapping(decisionDEF, "D", "E", "F");
    }

    @BeforeEach
    void beforeEach() {
        constraintsList = new ArrayList<>();
        decisionsList = new ArrayList<>();
        fields = new ProfileFields(Collections.emptyList());
        mappings = null;
    }

    private List<AtomicConstraint> constraintsList;
    private List<DecisionNode> decisionsList;
    private ProfileFields fields;
    private Map<RootLevelConstraint, Set<Field>> mappings;

    private void givenConstraints(AtomicConstraint... constraints) {
        constraintsList = Arrays.asList(constraints);
    }

    private void givenDecisions(DecisionNode... decisions) {
        decisionsList = Arrays.asList(decisions);
    }

    private void givenFields(String... fieldNames) {
        fields = new ProfileFields(
            Arrays.stream(fieldNames)
                .map(Field::new)
                .collect(Collectors.toList()));
    }

    private void getMappings() {
        mappings = new ConstraintToFieldMapper()
            .mapConstraintsToFields(new DecisionTree(
                new TreeConstraintNode(constraintsList, decisionsList),
                fields
            ));
    }

    private void expectMapping(AtomicConstraint constraint, String... fieldsAsString) {
        if (mappings == null)
            getMappings();

        final Field[] fields = Arrays.stream(fieldsAsString)
            .map(Field::new)
            .toArray(Field[]::new);

        Assert.assertThat(mappings.get(new RootLevelConstraint(constraint)), Matchers.hasItems(fields));
    }

    private void expectMapping(DecisionNode decision, String... fieldsAsString) {
        if (mappings == null)
            getMappings();

        final Field[] fields = Arrays.stream(fieldsAsString)
            .map(Field::new)
            .toArray(Field[]::new);

        Assert.assertThat(mappings.get(new RootLevelConstraint(decision)), Matchers.hasItems(fields));
    }

    private void expectMappingCount(int mappingsCount) {
        if (mappings == null)
            getMappings();

        Assert.assertThat(mappings, Matchers.aMapWithSize(mappingsCount));
    }
}
