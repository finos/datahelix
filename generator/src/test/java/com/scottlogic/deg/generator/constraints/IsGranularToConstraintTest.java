package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.IsGranularToConstraint;
import com.scottlogic.deg.generator.inputs.RuleInformation;
import com.scottlogic.deg.generator.restrictions.ParsedGranularity;
import com.scottlogic.deg.schemas.v3.RuleDTO;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;

public class IsGranularToConstraintTest {

    @Test
    public void testConstraintIsEqual() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");
        IsGranularToConstraint constraint1 = new IsGranularToConstraint(field1, new ParsedGranularity(new BigDecimal(0.1)), rules());
        IsGranularToConstraint constraint2 = new IsGranularToConstraint(field2, new ParsedGranularity(new BigDecimal(0.1)), rules());
        Assert.assertThat(constraint1, equalTo(constraint2));
    }

    @Test
    public void testConstraintIsNotEqualDueToField() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField2");
        IsGranularToConstraint constraint1 = new IsGranularToConstraint(field1, new ParsedGranularity(new BigDecimal(0.1)), rules());
        IsGranularToConstraint constraint2 = new IsGranularToConstraint(field2, new ParsedGranularity(new BigDecimal(0.1)), rules());
        Assert.assertNotEquals(constraint1, constraint2);
    }

    @Test
    public void testConstraintIsNotEqualDueToValue() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");
        IsGranularToConstraint constraint1 = new IsGranularToConstraint(field1, new ParsedGranularity(new BigDecimal(0.1)), rules());
        IsGranularToConstraint constraint2 = new IsGranularToConstraint(field2, new ParsedGranularity(new BigDecimal(1.0)), rules());
        Assert.assertNotEquals(constraint1, constraint2);
    }

    private static Set<RuleInformation> rules(){
        RuleDTO rule = new RuleDTO();
        rule.rule = "rules";
        return Collections.singleton(new RuleInformation(rule));
    }
}
