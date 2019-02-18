package com.scottlogic.deg.generator.restrictions;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;

class GranularityRestrictionsTests {
    @Test
    public void shouldBeEqualIfNumericScaleIsTheSame(){
        GranularityRestrictions a = restrictions(0.1);
        GranularityRestrictions b = restrictions(0.1);

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void shouldBeUnequalIfNumericScalesAreDifferent(){
        GranularityRestrictions a = restrictions(0.1);
        GranularityRestrictions b = restrictions(0.01);

        Assert.assertThat(a, not(equalTo(b)));
    }

    private static GranularityRestrictions restrictions(double numericScale){
        GranularityRestrictions restrictions = new GranularityRestrictions(
            ParsedGranularity.parse(BigDecimal.valueOf(numericScale))
        );

        return restrictions;
    }
}