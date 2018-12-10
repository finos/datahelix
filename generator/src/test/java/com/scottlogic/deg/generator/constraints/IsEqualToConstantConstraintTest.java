package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.IsEqualToConstantConstraint;
import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.Matchers.*;

public class IsEqualToConstantConstraintTest {

    @Test
    public void testConstraintIsEqual() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");
        IsEqualToConstantConstraint constraint1 = new IsEqualToConstantConstraint(field1, "abc", rule());
        IsEqualToConstantConstraint constraint2 = new IsEqualToConstantConstraint(field2, "abc", rule());
        Assert.assertThat(constraint1, equalTo(constraint2));
    }

    @Test
    public void testConstraintIsNotEqualDueToField() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField2");
        IsEqualToConstantConstraint constraint1 = new IsEqualToConstantConstraint(field1, "abc", rule());
        IsEqualToConstantConstraint constraint2 = new IsEqualToConstantConstraint(field2, "abc", rule());
        Assert.assertNotEquals(constraint1, constraint2);
    }

    @Test
    public void testConstraintIsNotEqualDueToValue() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");
        IsEqualToConstantConstraint constraint1 = new IsEqualToConstantConstraint(field1, "abc", rule());
        IsEqualToConstantConstraint constraint2 = new IsEqualToConstantConstraint(field2, "abcd", rule());
        Assert.assertNotEquals(constraint1, constraint2);
    }

    private static ConstraintRule rule(){
        return ConstraintRule.fromDescription("rule");
    }
}
