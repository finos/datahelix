package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.IsInSetConstraint;
import com.scottlogic.deg.generator.constraints.atomic.IsNullConstraint;
import com.scottlogic.deg.generator.inputs.RuleInformation;
import com.scottlogic.deg.schemas.v3.RuleDTO;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;

public class NotConstraintTest {

    @Test
    public void testConstraintIsEqual() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");
        Constraint constraint1 = new IsNullConstraint(field1, rules()).negate();
        Constraint constraint2 = new IsNullConstraint(field2, rules()).negate();
        Assert.assertThat(constraint1, equalTo(constraint2));
    }

    @Test
    public void testConstraintIsEqualRecursively() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");
        Constraint constraint1 = new IsNullConstraint(field1, rules()).negate();
        Constraint constraint2 = new IsNullConstraint(field2, rules()).negate().negate().negate();
        Assert.assertThat(constraint1, equalTo(constraint2));
    }

    @Test
    public void testConstraintIsEqualRecursivelySameLevel() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");
        Constraint constraint1 = new IsNullConstraint(field1, rules()).negate().negate().negate();
        Constraint constraint2 = new IsNullConstraint(field2, rules()).negate().negate().negate();
        Assert.assertThat(constraint1, equalTo(constraint2));
    }

    @Test
    public void testConstraintIsNotEqualDueToField() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField2");
        Constraint constraint1 = new IsNullConstraint(field1, rules()).negate();
        Constraint constraint2 = new IsNullConstraint(field2, rules()).negate();
        Assert.assertNotEquals(constraint1, constraint2);
    }

    @Test
    public void testConstraintIsNotEqualDueToValue() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");
        Constraint constraint1 = new IsInSetConstraint(field1, Collections.singleton("abc"), rules()).negate();
        Constraint constraint2 = new IsInSetConstraint(field2, Collections.singleton("abcd"), rules()).negate();
        Assert.assertNotEquals(constraint1, constraint2);
    }

    @Test
    public void testConstraintIsNotEqualRecursively() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");
        Constraint constraint1 = new IsNullConstraint(field1, rules()).negate();
        Constraint constraint2 = new IsNullConstraint(field2, rules()).negate().negate();
        Assert.assertNotEquals(constraint1, constraint2);
    }

    private static Set<RuleInformation> rules(){
        RuleDTO rule = new RuleDTO();
        rule.rule = "rules";
        return Collections.singleton(new RuleInformation(rule));
    }
}
