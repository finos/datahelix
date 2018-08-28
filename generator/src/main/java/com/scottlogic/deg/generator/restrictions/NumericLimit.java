package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.utils.NumberUtils;

import java.math.BigDecimal;

public class NumericLimit<T extends Number> {
    private final T limit;
    private final boolean isInclusive;

    public NumericLimit(T limit, boolean isInclusive) {
        this.limit = limit;
        this.isInclusive = isInclusive;
    }

    public T getLimit() {
        return limit;
    }

    public boolean isInclusive() {
        return isInclusive;
    }

    public static NumericLimit<Integer> bigDecimalToInteger(NumericLimit<BigDecimal> decimalLimit) {
        if (!NumberUtils.isInteger(decimalLimit.limit))
            throw new IllegalArgumentException("Can't convert limit: " + decimalLimit.limit + " to integer");

        return new NumericLimit<>(
            decimalLimit.limit.intValue(),
            decimalLimit.isInclusive);
    }
}
