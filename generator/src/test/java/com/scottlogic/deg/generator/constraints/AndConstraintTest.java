package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.IsNullConstraint;
import com.scottlogic.deg.generator.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.generator.inputs.RuleInformation;
import com.scottlogic.deg.schemas.v3.RuleDTO;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;

public class AndConstraintTest {

    @Test
    public void testConstraintIsEqual() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");

        Field field3 = new Field("TestField");
        Field field4 = new Field("TestField");
        AndConstraint constraint1 = new AndConstraint(new IsNullConstraint(field1, rules()), new IsNullConstraint(field2, rules()));
        AndConstraint constraint2 = new AndConstraint(new IsNullConstraint(field3, rules()), new IsNullConstraint(field4, rules()));
        Assert.assertThat(constraint1, equalTo(constraint2));
    }

    @Test
    public void testConstraintIsEqualRecursively() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");

        Field field3 = new Field("TestField");
        Field field4 = new Field("TestField");
        AndConstraint constraint1 = new AndConstraint(new AndConstraint(new IsNullConstraint(field1, rules()), new IsNullConstraint(field2, rules())));
        AndConstraint constraint2 = new AndConstraint(new AndConstraint(new IsNullConstraint(field3, rules()), new IsNullConstraint(field4, rules())));
        Assert.assertThat(constraint1, equalTo(constraint2));
    }

    private static Set<RuleInformation> rules(){
        RuleDTO rule = new RuleDTO();
        rule.rule = "rules";
        return Collections.singleton(new RuleInformation(rule));
    }
}
