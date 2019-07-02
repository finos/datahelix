package com.scottlogic.deg.common.profile.constraints.grammatical;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraints.Constraint;
import com.scottlogic.deg.common.profile.constraints.atomic.IsInSetConstraint;
import com.scottlogic.deg.common.profile.constraints.atomic.IsNullConstraint;
import com.scottlogic.deg.common.profile.RuleInformation;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

public class NotConstraintTests {

    @Test
    public void testConstraintIsEqual() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");
        Constraint constraint1 = new IsNullConstraint(field1).negate();
        Constraint constraint2 = new IsNullConstraint(field2).negate();
        Assert.assertThat(constraint1, Matchers.equalTo(constraint2));
    }

    @Test
    public void testConstraintIsEqualRecursively() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");
        Constraint constraint1 = new IsNullConstraint(field1).negate();
        Constraint constraint2 = new IsNullConstraint(field2).negate().negate().negate();
        Assert.assertThat(constraint1, Matchers.equalTo(constraint2));
    }

    @Test
    public void testConstraintIsEqualRecursivelySameLevel() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");
        Constraint constraint1 = new IsNullConstraint(field1).negate().negate().negate();
        Constraint constraint2 = new IsNullConstraint(field2).negate().negate().negate();
        Assert.assertThat(constraint1, Matchers.equalTo(constraint2));
    }

    @Test
    public void testConstraintIsNotEqualDueToField() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField2");
        Constraint constraint1 = new IsNullConstraint(field1).negate();
        Constraint constraint2 = new IsNullConstraint(field2).negate();
        Assert.assertNotEquals(constraint1, constraint2);
    }

    @Test
    public void testConstraintIsNotEqualDueToValue() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");
        Constraint constraint1 = new IsInSetConstraint(field1, Collections.singleton("abc")).negate();
        Constraint constraint2 = new IsInSetConstraint(field2, Collections.singleton("abcd")).negate();
        Assert.assertNotEquals(constraint1, constraint2);
    }

    @Test
    public void testConstraintIsNotEqualRecursively() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");
        Constraint constraint1 = new IsNullConstraint(field1).negate();
        Constraint constraint2 = new IsNullConstraint(field2).negate().negate();
        Assert.assertNotEquals(constraint1, constraint2);
    }

}
