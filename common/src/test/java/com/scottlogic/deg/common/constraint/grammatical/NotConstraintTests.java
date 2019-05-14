package com.scottlogic.deg.common.constraint.grammatical;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.constraint.Constraint;
import com.scottlogic.deg.common.constraint.atomic.IsInSetConstraint;
import com.scottlogic.deg.common.constraint.atomic.IsNullConstraint;
import com.scottlogic.deg.common.profile.RuleInformation;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;

public class NotConstraintTests {

    @Test
    public void testConstraintIsEqual() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");
        Constraint constraint1 = new IsNullConstraint(field1, rules()).negate();
        Constraint constraint2 = new IsNullConstraint(field2, rules()).negate();
        Assert.assertThat(constraint1, Matchers.equalTo(constraint2));
    }

    @Test
    public void testConstraintIsEqualRecursively() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");
        Constraint constraint1 = new IsNullConstraint(field1, rules()).negate();
        Constraint constraint2 = new IsNullConstraint(field2, rules()).negate().negate().negate();
        Assert.assertThat(constraint1, Matchers.equalTo(constraint2));
    }

    @Test
    public void testConstraintIsEqualRecursivelySameLevel() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");
        Constraint constraint1 = new IsNullConstraint(field1, rules()).negate().negate().negate();
        Constraint constraint2 = new IsNullConstraint(field2, rules()).negate().negate().negate();
        Assert.assertThat(constraint1, Matchers.equalTo(constraint2));
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
        return Collections.singleton(new RuleInformation());
    }
}