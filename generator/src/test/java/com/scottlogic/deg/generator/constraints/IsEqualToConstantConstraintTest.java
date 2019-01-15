package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.IsEqualToConstantConstraint;
import com.scottlogic.deg.generator.inputs.RuleInformation;
import com.scottlogic.deg.schemas.v3.RuleDTO;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Collections;
import java.util.Set;

import static org.hamcrest.Matchers.*;

public class IsEqualToConstantConstraintTest {

    @Test
    public void testConstraintIsEqual() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");
        IsEqualToConstantConstraint constraint1 = new IsEqualToConstantConstraint(field1, "abc", rules());
        IsEqualToConstantConstraint constraint2 = new IsEqualToConstantConstraint(field2, "abc", rules());
        Assert.assertThat(constraint1, equalTo(constraint2));
    }

    @Test
    public void testConstraintIsNotEqualDueToField() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField2");
        IsEqualToConstantConstraint constraint1 = new IsEqualToConstantConstraint(field1, "abc", rules());
        IsEqualToConstantConstraint constraint2 = new IsEqualToConstantConstraint(field2, "abc", rules());
        Assert.assertNotEquals(constraint1, constraint2);
    }

    @Test
    public void testConstraintIsNotEqualDueToValue() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");
        IsEqualToConstantConstraint constraint1 = new IsEqualToConstantConstraint(field1, "abc", rules());
        IsEqualToConstantConstraint constraint2 = new IsEqualToConstantConstraint(field2, "abcd", rules());
        Assert.assertNotEquals(constraint1, constraint2);
    }

    @Test
    public void testConstraintThrowsIfGivenNull(){
        Field field1 = new Field("TestField");

        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> new IsEqualToConstantConstraint(field1, null, rules()));
    }

    private static Set<RuleInformation> rules(){
        RuleDTO rule = new RuleDTO();
        rule.rule = "rules";
        return Collections.singleton(new RuleInformation(rule));
    }
}
