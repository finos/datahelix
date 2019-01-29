package com.scottlogic.deg.generator.restrictions;

import java.math.BigDecimal;
import java.util.Objects;

public class NumericRestrictions {

    public static boolean isNumeric(Object o){
        return o instanceof Number;
    }

    public NumericLimit<BigDecimal> min;
    public NumericLimit<BigDecimal> max;

    public boolean match(Object o) {
        if (!NumericRestrictions.isNumeric(o)) {
            return false;
        }

        BigDecimal n = new BigDecimal(o.toString());

        if(min != null){
            if(n.compareTo(min.getLimit()) < (min.isInclusive() ? 0 : 1))
            {
                return false;
            }
        }

        if(max != null){
            if(n.compareTo(max.getLimit()) > (max.isInclusive() ? 0 : -1))
            {
                return false;
            }
        }

        return true;
    }

    public boolean areLimitValuesInteger() {
        if (min == null || max == null) {
            return false;
        }

        // If either of the min or max values have decimal points or if the sign differs when converting to an integer
        // the value is not an integer
        BigDecimal minLimit = min.getLimit();
        BigDecimal maxLimit = max.getLimit();
        if (minLimit.scale() > 0 || maxLimit.scale() > 0 ||
            minLimit.signum() != Integer.signum(minLimit.intValue()) ||
            maxLimit.signum() != Integer.signum(maxLimit.intValue())) {
            return false;
        }

        return (minLimit.toBigInteger().signum() == 0 || minLimit.intValue() != 0) &&
               (maxLimit.toBigInteger().signum() == 0 || maxLimit.intValue() != 0);
    }

    @Override
    public String toString() {
        return String.format(
            "%s%s%s",
            min != null ? min.toString(">") : "",
            min != null && max != null ? " and " : "",
            max != null ? max.toString("<") : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumericRestrictions that = (NumericRestrictions) o;
        return Objects.equals(min, that.min) &&
            Objects.equals(max, that.max);
    }

    @Override
    public int hashCode() {
        return Objects.hash(min, max);
    }
}
