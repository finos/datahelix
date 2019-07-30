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

package com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy;

import com.scottlogic.deg.generator.ConstraintBuilder;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.common.profile.Rule;
import com.scottlogic.deg.common.profile.constraints.Constraint;
import com.scottlogic.deg.generator.decisiontree.ProfileDecisionTreeFactory;
import com.scottlogic.deg.common.profile.RuleInformation;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.mock;
import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

public class TestHierarchicalDependencyFixFieldStrategy {

    @Test
    public void testDependencyOfFieldsWithSameSetSizes() {
        String TEST_FIELD_1 = "Controlling Field";
        String TEST_FIELD_2 = "Dependent Field";

        List<Field> fields = getFields(TEST_FIELD_1, TEST_FIELD_2);
        List<Constraint> constraints = new ConstraintBuilder(fields)
            .addInSetConstraint(TEST_FIELD_1, Arrays.asList("CO", "CR", "CU"))
            .addInSetConstraint(TEST_FIELD_2, Arrays.asList("A", "B", "C"))
            .addConditionalConstraint(
                new ConstraintBuilder(fields).addEqualToConstraint(TEST_FIELD_1, "CO").build(),
                new ConstraintBuilder(fields).addEqualToConstraint(TEST_FIELD_2, "A").build()
            )
            .build();
        List<Field> priorities = getPriorities(fields, constraints);
        Assert.assertEquals(priorities.get(0).name, TEST_FIELD_1);
        Assert.assertEquals(priorities.get(1).name, TEST_FIELD_2);
    }

    @Test
    public void testHierarchicalDependencyOverDifferingSetSizes() {
        String controlling1 = "Controlling Field 1";
        String controlling2 = "Controlling Field 2";
        String dependent = "Dependent Field";
        List<Field> fields = getFields(controlling1, controlling2, dependent);

        // controlling1 affects controlling2 affects dependent
        List<Constraint> constraints = new ConstraintBuilder(fields)
            .addInSetConstraint(controlling1, Arrays.asList("CO", "CR", "CU", "EQ"))
            .addInSetConstraint(controlling2, Arrays.asList("B", "M", "N"))
            .addInSetConstraint(dependent, Arrays.asList("A", "B", "C"))
            .addConditionalConstraint(
                new ConstraintBuilder(fields).addEqualToConstraint(controlling1, "CO").build(),
                new ConstraintBuilder(fields).addEqualToConstraint(controlling2, "B").build()
            )
            .addConditionalConstraint(
                new ConstraintBuilder(fields).addEqualToConstraint(controlling2, "M").build(),
                new ConstraintBuilder(fields).addEqualToConstraint(dependent, "A").build()
            )
            .build();
        List<Field> actual = getPriorities(fields, constraints);

        List<Field> expected = Arrays.asList(new Field(controlling2), new Field(controlling1), new Field(dependent));

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void testIndependentHierarchicalDependencies() {
        String TEST_FIELD_1 = "Controlling Field Bravo";
        String TEST_FIELD_2 = "Controlling Field Alpha";
        String TEST_FIELD_3 = "Dependent Field Charlie";
        String TEST_FIELD_4 = "Dependent Field Delta";
        List<Field> fields = getFields(TEST_FIELD_1, TEST_FIELD_2, TEST_FIELD_3, TEST_FIELD_4);

        // TEST_FIELD_1 affects TEST_FIELD_3, TEST_FIELD_2 affects TEST_FIELD_4
        List<Constraint> constraints = new ConstraintBuilder(fields)
            .addInSetConstraint(TEST_FIELD_1, Arrays.asList("CO", "CR", "CU"))
            .addInSetConstraint(TEST_FIELD_2, Arrays.asList("B", "M", "N"))
            .addInSetConstraint(TEST_FIELD_3, Arrays.asList("A", "B", "C"))
            .addInSetConstraint(TEST_FIELD_4, Arrays.asList("D", "E", "F"))
            .addConditionalConstraint(
                new ConstraintBuilder(fields).addEqualToConstraint(TEST_FIELD_1, "CO").build(),
                new ConstraintBuilder(fields).addEqualToConstraint(TEST_FIELD_3, "B").build()
            )
            .addConditionalConstraint(
                new ConstraintBuilder(fields).addEqualToConstraint(TEST_FIELD_2, "M").build(),
                new ConstraintBuilder(fields).addEqualToConstraint(TEST_FIELD_4, "D").build()
            )
            .build();
        List<Field> priorities = getPriorities(fields, constraints);

        Assert.assertEquals(priorities.get(0).name, TEST_FIELD_2);
        Assert.assertEquals(priorities.get(1).name, TEST_FIELD_1);
        Assert.assertEquals(priorities.get(2).name, TEST_FIELD_3);
        Assert.assertEquals(priorities.get(3).name, TEST_FIELD_4);
    }

    @Test
    public void testHierarchicalDependencyAndOneIndependentField() {
        String controlling = "Controlling Field";
        String independent = "Independent Field";
        String dependent1 = "Dependent Field 1";
        String dependent2 = "Dependent Field 2";
        List<Field> fields = getFields(controlling, independent, dependent1, dependent2);

        List<Constraint> constraints = new ConstraintBuilder(fields)
            .addInSetConstraint(controlling, Arrays.asList("CO", "CR", "CU"))
            .addInSetConstraint(independent, Arrays.asList("B", "M", "N"))
            .addInSetConstraint(dependent1, Arrays.asList("A", "B", "C"))
            .addInSetConstraint(dependent2, Arrays.asList("D", "E", "F"))
            .addConditionalConstraint(
                new ConstraintBuilder(fields).addEqualToConstraint(controlling, "CO").build(),
                new ConstraintBuilder(fields).addEqualToConstraint(dependent1, "B").build()
            )
            .addConditionalConstraint(
                new ConstraintBuilder(fields).addEqualToConstraint(controlling, "CO").build(),
                new ConstraintBuilder(fields).addEqualToConstraint(dependent2, "D").build()
            )
            .build();

        List<Field> actual = getPriorities(fields, constraints);

        List<Field> expected = Arrays.asList(new Field(controlling), new Field(dependent2), new Field(dependent1), new Field(independent));

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void testAlphabeticalOrdering() {
        String TEST_FIELD_1 = "Z";
        String TEST_FIELD_2 = "A";
        List<Field> fields = getFields(TEST_FIELD_1, TEST_FIELD_2);
        List<Constraint> constraints = new ConstraintBuilder(fields)
            .addInSetConstraint(TEST_FIELD_1, Arrays.asList("CO", "CR", "CU", "EQ"))
            .addInSetConstraint(TEST_FIELD_2, Arrays.asList("B", "M", "N", "X"))
            .build();
        List<Field> priorities = getPriorities(fields, constraints);
        Assert.assertEquals(priorities.get(0).name, TEST_FIELD_2);
        Assert.assertEquals(priorities.get(1).name, TEST_FIELD_1);
    }

    @Test
    public void testCircularDependency() {
        String TEST_FIELD_1 = "Circular Field B";
        String TEST_FIELD_2 = "Circular Field A";
        List<Field> fields = getFields(TEST_FIELD_1, TEST_FIELD_2);

        List<Constraint> constraints = new ConstraintBuilder(fields)
            .addInSetConstraint(TEST_FIELD_1, Arrays.asList("CO", "CR", "CU"))
            .addInSetConstraint(TEST_FIELD_2, Arrays.asList("B", "M", "N"))
            .addConditionalConstraint(
                new ConstraintBuilder(fields).addNullConstraint(TEST_FIELD_1).build(),
                new ConstraintBuilder(fields).addNullConstraint(TEST_FIELD_2).build()
            )
            .addConditionalConstraint(
                new ConstraintBuilder(fields).addNullConstraint(TEST_FIELD_2).build(),
                new ConstraintBuilder(fields).addNullConstraint(TEST_FIELD_1).build()
            )
            .build();
        List<Field> priorities = getPriorities(fields, constraints);

        // Sorted alphabetically as no issue with circular dependency
        Assert.assertEquals(priorities.get(0).name, TEST_FIELD_2);
        Assert.assertEquals(priorities.get(1).name, TEST_FIELD_1);
    }

    private List<Field> getFields(String ...names) {
        return Arrays.stream(names)
            .map(Field::new)
            .collect(Collectors.toList());
    }

    private List<Field> getPriorities(List<Field> fields, List<Constraint> constraints) {
        List<Rule> rules = Collections.singletonList(new Rule(new RuleInformation(), constraints));
        String schemaVersion = "0.1";
        Profile profile = new Profile(schemaVersion, fields, rules);

        DecisionTree tree = new ProfileDecisionTreeFactory().analyse(profile);

        FieldAppearanceFixingStrategy strategy =
            new FieldAppearanceFixingStrategy(tree.getRootNode());
        return strategy.fieldsInFixingOrder;
    }
}
