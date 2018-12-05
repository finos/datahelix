package com.scottlogic.deg.generator.walker.reductive.field_selection_strategy;

import com.scottlogic.deg.generator.ConstraintBuilder;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.constraints.IConstraint;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TestHierarchicalDependencyFixFieldStrategy {

    @Test
    public void testDependencyOfFieldsWithSameSetSizes() {
        String TEST_FIELD_1 = "Controlling Field";
        String TEST_FIELD_2 = "Dependent Field";

        List<Field> fields = getFields(TEST_FIELD_1, TEST_FIELD_2);
        List<IConstraint> constraints = new ConstraintBuilder(fields)
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
        String TEST_FIELD_1 = "Controlling Field 1";
        String TEST_FIELD_2 = "Controlling Field 2";
        String TEST_FIELD_3 = "Dependent Field";
        List<Field> fields = getFields(TEST_FIELD_1, TEST_FIELD_2, TEST_FIELD_3);

        // TEST_FIELD_1 affects TEST_FIELD_2 affects TEST_FIELD_3
        List<IConstraint> constraints = new ConstraintBuilder(fields)
            .addInSetConstraint(TEST_FIELD_1, Arrays.asList("CO", "CR", "CU", "EQ"))
            .addInSetConstraint(TEST_FIELD_2, Arrays.asList("B", "M", "N"))
            .addInSetConstraint(TEST_FIELD_3, Arrays.asList("A", "B", "C"))
            .addConditionalConstraint(
                new ConstraintBuilder(fields).addEqualToConstraint(TEST_FIELD_1, "CO").build(),
                new ConstraintBuilder(fields).addEqualToConstraint(TEST_FIELD_2, "B").build()
            )
            .addConditionalConstraint(
                new ConstraintBuilder(fields).addEqualToConstraint(TEST_FIELD_2, "M").build(),
                new ConstraintBuilder(fields).addEqualToConstraint(TEST_FIELD_3, "A").build()
            )
            .addConditionalConstraint(
                new ConstraintBuilder(fields).addEqualToConstraint(TEST_FIELD_2, "B").build(),
                new ConstraintBuilder(fields).addEqualToConstraint(TEST_FIELD_3, "C").build()
            )
            .build();
        List<Field> priorities = getPriorities(fields, constraints);

        Assert.assertEquals(priorities.get(0).name, TEST_FIELD_1);
        Assert.assertEquals(priorities.get(1).name, TEST_FIELD_2);
        Assert.assertEquals(priorities.get(2).name, TEST_FIELD_3);
    }

    @Test
    public void testHierarchicalDependencyWithDifferingSetSizes() {
        String TEST_FIELD_1 = "Controlling Field 1";
        String TEST_FIELD_2 = "Controlling Field 2";
        String TEST_FIELD_3 = "Dependent Field";
        List<Field> fields = getFields(TEST_FIELD_1, TEST_FIELD_2, TEST_FIELD_3);

        // TEST_FIELD_1 and TEST_FIELD_2 affect TEST_FIELD_3
        List<IConstraint> constraints = new ConstraintBuilder(fields)
            .addInSetConstraint(TEST_FIELD_1, Arrays.asList("CO", "CR", "CU", "EQ"))
            .addInSetConstraint(TEST_FIELD_2, Arrays.asList("B", "M", "N"))
            .addInSetConstraint(TEST_FIELD_3, Arrays.asList("A", "B", "C"))
            .addConditionalConstraint(
                new ConstraintBuilder(fields).addEqualToConstraint(TEST_FIELD_1, "CO").build(),
                new ConstraintBuilder(fields).addEqualToConstraint(TEST_FIELD_3, "B").build()
            )
            .addConditionalConstraint(
                new ConstraintBuilder(fields).addEqualToConstraint(TEST_FIELD_2, "B").build(),
                new ConstraintBuilder(fields).addEqualToConstraint(TEST_FIELD_3, "C").build()
            )
            .build();
        List<Field> priorities = getPriorities(fields, constraints);

        // TEST_FIELD 2 is smaller set
        Assert.assertEquals(priorities.get(0).name, TEST_FIELD_2);
        Assert.assertEquals(priorities.get(1).name, TEST_FIELD_1);
        Assert.assertEquals(priorities.get(2).name, TEST_FIELD_3);
    }

    @Test
    public void testIndependentHierarchicalDependency() {
        String TEST_FIELD_1 = "Controlling Field Bravo";
        String TEST_FIELD_2 = "Controlling Field Alpha";
        String TEST_FIELD_3 = "Dependent Field Charlie";
        String TEST_FIELD_4 = "Dependent Field Delta";
        List<Field> fields = getFields(TEST_FIELD_1, TEST_FIELD_2, TEST_FIELD_3, TEST_FIELD_4);

        // TEST_FIELD_1 affects TEST_FIELD_3, TEST_FIELD_2 affects TEST_FIELD_4
        List<IConstraint> constraints = new ConstraintBuilder(fields)
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
    public void testAlphabeticalOrdering() {
        String TEST_FIELD_1 = "Z";
        String TEST_FIELD_2 = "A";
        List<Field> fields = getFields(TEST_FIELD_1, TEST_FIELD_2);
        List<IConstraint> constraints = new ConstraintBuilder(fields)
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

        List<IConstraint> constraints = new ConstraintBuilder(fields)
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

    private List<Field> getPriorities(List<Field> fields, List<IConstraint> constraints) {
        List<Rule> rules = Collections.singletonList(new Rule("Test rule", constraints));
        Profile profile = new Profile(fields, rules);
        HierarchicalDependencyFixFieldStrategy strategy = new HierarchicalDependencyFixFieldStrategy(profile);
        return strategy.getFieldFixingPriorityList();
    }

}
