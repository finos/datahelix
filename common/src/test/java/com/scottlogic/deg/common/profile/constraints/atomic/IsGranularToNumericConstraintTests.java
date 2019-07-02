package com.scottlogic.deg.common.profile.constraints.atomic;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.RuleInformation;
import com.scottlogic.deg.common.profile.constraintdetail.ParsedGranularity;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;

public class IsGranularToNumericConstraintTests {

    @Test
    public void testConstraintIsEqual() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");
        IsGranularToNumericConstraint constraint1 = new IsGranularToNumericConstraint(field1, new ParsedGranularity(new BigDecimal(0.1)));
        IsGranularToNumericConstraint constraint2 = new IsGranularToNumericConstraint(field2, new ParsedGranularity(new BigDecimal(0.1)));
        Assert.assertThat(constraint1, Matchers.equalTo(constraint2));
    }

    @Test
    public void testConstraintIsNotEqualDueToField() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField2");
        IsGranularToNumericConstraint constraint1 = new IsGranularToNumericConstraint(field1, new ParsedGranularity(new BigDecimal(0.1)));
        IsGranularToNumericConstraint constraint2 = new IsGranularToNumericConstraint(field2, new ParsedGranularity(new BigDecimal(0.1)));
        Assert.assertNotEquals(constraint1, constraint2);
    }

    @Test
    public void testConstraintIsNotEqualDueToValue() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");
        IsGranularToNumericConstraint constraint1 = new IsGranularToNumericConstraint(field1, new ParsedGranularity(new BigDecimal(0.1)));
        IsGranularToNumericConstraint constraint2 = new IsGranularToNumericConstraint(field2, new ParsedGranularity(new BigDecimal(1.0)));
        Assert.assertNotEquals(constraint1, constraint2);
    }

}
