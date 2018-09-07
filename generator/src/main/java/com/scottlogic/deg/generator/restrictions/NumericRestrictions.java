package com.scottlogic.deg.generator.restrictions;

import java.math.BigDecimal;

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
}
