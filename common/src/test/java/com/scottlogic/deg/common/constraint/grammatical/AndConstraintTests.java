package com.scottlogic.deg.common.constraint.grammatical;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.constraint.atomic.IsNullConstraint;
import com.scottlogic.deg.common.profile.RuleInformation;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;

public class AndConstraintTests {

    @Test
    public void testConstraintIsEqual() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");

        Field field3 = new Field("TestField");
        Field field4 = new Field("TestField");
        AndConstraint constraint1 = new AndConstraint(new IsNullConstraint(field1, rules()), new IsNullConstraint(field2, rules()));
        AndConstraint constraint2 = new AndConstraint(new IsNullConstraint(field3, rules()), new IsNullConstraint(field4, rules()));
        Assert.assertThat(constraint1, Matchers.equalTo(constraint2));
    }

    @Test
    public void testConstraintIsEqualRecursively() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");

        Field field3 = new Field("TestField");
        Field field4 = new Field("TestField");
        AndConstraint constraint1 = new AndConstraint(new AndConstraint(new IsNullConstraint(field1, rules()), new IsNullConstraint(field2, rules())));
        AndConstraint constraint2 = new AndConstraint(new AndConstraint(new IsNullConstraint(field3, rules()), new IsNullConstraint(field4, rules())));
        Assert.assertThat(constraint1, Matchers.equalTo(constraint2));
    }

    private static Set<RuleInformation> rules(){
        return Collections.singleton(new RuleInformation());
    }
}
