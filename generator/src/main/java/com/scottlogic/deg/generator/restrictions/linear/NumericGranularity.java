package com.scottlogic.deg.generator.restrictions.linear;

import com.scottlogic.deg.common.profile.constraintdetail.ParsedGranularity;

import java.math.BigDecimal;

public class NumericGranularity implements Granularity<BigDecimal> {

    public final int decimalPlaces;

    public NumericGranularity(int decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    @Override
    public boolean isCorrectScale(BigDecimal value) {
        return value.stripTrailingZeros().scale() <= decimalPlaces;
    }

    @Override
    public NumericGranularity merge(Granularity<BigDecimal> otherGranularity) {
        NumericGranularity other = (NumericGranularity) otherGranularity;
        return decimalPlaces <= other.decimalPlaces ? this : other;
    }
}
