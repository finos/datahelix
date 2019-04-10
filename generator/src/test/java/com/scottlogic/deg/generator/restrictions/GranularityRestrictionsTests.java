package com.scottlogic.deg.generator.restrictions;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;

class GranularityRestrictionsTests {
    @Test
    public void shouldBeEqualIfNumericScaleIsTheSame(){
        NumericRestrictions a = restrictions(0.1);
        NumericRestrictions b = restrictions(0.1);

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void shouldBeUnequalIfNumericScalesAreDifferent(){
        NumericRestrictions a = restrictions(0.1);
        NumericRestrictions b = restrictions(0.01);

        Assert.assertThat(a, not(equalTo(b)));
    }

    private static NumericRestrictions restrictions(double numericScale){
        NumericRestrictions restrictions = new NumericRestrictions(
            ParsedGranularity.parse(BigDecimal.valueOf(numericScale))
        );

        return restrictions;
    }
}