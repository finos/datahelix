package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.IsEqualToConstantConstraint;
import com.scottlogic.deg.generator.constraints.atomic.IsNullConstraint;
import com.scottlogic.deg.generator.inputs.RuleInformation;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class NotConstraintTest {

    @Test
    public void testConstraintIsEqual() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");
        Constraint constraint1 = new IsNullConstraint(field1, rule()).negate();
        Constraint constraint2 = new IsNullConstraint(field2, rule()).negate();
        Assert.assertThat(constraint1, equalTo(constraint2));
    }

    @Test
    public void testConstraintIsEqualRecursively() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");
        Constraint constraint1 = new IsNullConstraint(field1, rule()).negate();
        Constraint constraint2 = new IsNullConstraint(field2, rule()).negate().negate().negate();
        Assert.assertThat(constraint1, equalTo(constraint2));
    }

    @Test
    public void testConstraintIsEqualRecursivelySameLevel() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");
        Constraint constraint1 = new IsNullConstraint(field1, rule()).negate().negate().negate();
        Constraint constraint2 = new IsNullConstraint(field2, rule()).negate().negate().negate();
        Assert.assertThat(constraint1, equalTo(constraint2));
    }

    @Test
    public void testConstraintIsNotEqualDueToField() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField2");
        Constraint constraint1 = new IsNullConstraint(field1, rule()).negate();
        Constraint constraint2 = new IsNullConstraint(field2, rule()).negate();
        Assert.assertNotEquals(constraint1, constraint2);
    }

    @Test
    public void testConstraintIsNotEqualDueToValue() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");
        Constraint constraint1 = new IsEqualToConstantConstraint(field1, "abc", rule()).negate();
        Constraint constraint2 = new IsEqualToConstantConstraint(field2, "abcd", rule()).negate();
        Assert.assertNotEquals(constraint1, constraint2);
    }

    @Test
    public void testConstraintIsNotEqualRecursively() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");
        Constraint constraint1 = new IsNullConstraint(field1, rule()).negate();
        Constraint constraint2 = new IsNullConstraint(field2, rule()).negate().negate();
        Assert.assertNotEquals(constraint1, constraint2);
    }

    private static RuleInformation rule(){
        return RuleInformation.fromDescription("rule");
    }
}
