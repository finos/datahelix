package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.ConstraintBuilder;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.constraints.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
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
    public void testHierarchicalDependencyOfFieldsWithDifferingSetSizes() {
        String TEST_FIELD_1 = "Controlling Field 1";
        String TEST_FIELD_2 = "Controlling Field 2";
        String TEST_FIELD_3 = "Dependent Field";
        List<Field> fields = getFields(TEST_FIELD_1, TEST_FIELD_2, TEST_FIELD_3);

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

        // TEST_FIELD_1 affects TEST_FIELD_2 affects TEST_FIELD_3
        Assert.assertEquals(priorities.get(0).name, TEST_FIELD_1);
        Assert.assertEquals(priorities.get(1).name, TEST_FIELD_2);
        Assert.assertEquals(priorities.get(2).name, TEST_FIELD_3);
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
