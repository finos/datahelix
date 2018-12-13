package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.IsNullConstraint;
import com.scottlogic.deg.generator.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.generator.inputs.RuleInformation;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class AndConstraintTest {

    @Test
    public void testConstraintIsEqual() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");

        Field field3 = new Field("TestField");
        Field field4 = new Field("TestField");
        AndConstraint constraint1 = new AndConstraint(new IsNullConstraint(field1, rule()), new IsNullConstraint(field2, rule()));
        AndConstraint constraint2 = new AndConstraint(new IsNullConstraint(field3, rule()), new IsNullConstraint(field4, rule()));
        Assert.assertThat(constraint1, equalTo(constraint2));
    }

    @Test
    public void testConstraintIsEqualRecursively() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");

        Field field3 = new Field("TestField");
        Field field4 = new Field("TestField");
        AndConstraint constraint1 = new AndConstraint(new AndConstraint(new IsNullConstraint(field1, rule()), new IsNullConstraint(field2, rule())));
        AndConstraint constraint2 = new AndConstraint(new AndConstraint(new IsNullConstraint(field3, rule()), new IsNullConstraint(field4, rule())));
        Assert.assertThat(constraint1, equalTo(constraint2));
    }

    private static RuleInformation rule(){
        return RuleInformation.fromDescription("rule");
    }
}
