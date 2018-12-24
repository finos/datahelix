package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.IsEqualToConstantConstraint;
import com.scottlogic.deg.generator.constraints.atomic.IsInSetConstraint;
import com.scottlogic.deg.generator.inputs.RuleInformation;
import com.scottlogic.deg.generator.restrictions.NullRestrictions;
import com.scottlogic.deg.schemas.v3.RuleDTO;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public class IsInSetConstraintTests {

    @Test
    public void testConstraintThrowsIfGivenEmptySet(){
        Field field1 = new Field("TestField");
        try {
            new IsInSetConstraint(field1, Collections.emptySet(), rules());
        } catch (Exception e) {
            Assert.assertThat(e, is(instanceOf(IllegalArgumentException.class)));
            return;
        }

        Assert.fail("Exception was expected but not thrown");
    }

    @Test
    public void testConstraintThrowsIfGivenNullInASet(){
        Field field1 = new Field("TestField");
        try {
            new IsInSetConstraint(field1, Collections.singleton(null), rules());
        } catch (Exception e) {
            Assert.assertThat(e, is(instanceOf(IllegalArgumentException.class)));
            return;
        }

        Assert.fail("Exception was expected but not thrown");
    }

    @Test
    public void testConstraintThrowsNothingIfGivenAValidSet(){
        Field field1 = new Field("TestField");
        new IsInSetConstraint(field1, Collections.singleton("foo"), rules());
    }

    private static Set<RuleInformation> rules(){
        RuleDTO rule = new RuleDTO();
        rule.rule = "rules";
        return Collections.singleton(new RuleInformation(rule));
    }
}
