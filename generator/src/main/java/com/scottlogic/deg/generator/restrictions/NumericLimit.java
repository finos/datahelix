package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.common.util.NumberUtils;

import java.math.BigDecimal;
import java.util.Objects;

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

    public String toString(String operator) {
        return String.format(
            "%s%s %s",
            operator,
            this.isInclusive ? "=" : "",
            this.limit.toString()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumericLimit<?> that = (NumericLimit<?>) o;
        return isInclusive == that.isInclusive &&
            Objects.equals(limit, that.limit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(limit, isInclusive);
    }
}
