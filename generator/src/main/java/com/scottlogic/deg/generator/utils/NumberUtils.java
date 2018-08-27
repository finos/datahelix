package com.scottlogic.deg.generator.utils;

import java.math.BigDecimal;

public class NumberUtils {
    public static boolean isInteger(BigDecimal decimalValue) {
        // stolen from https://stackoverflow.com/questions/1078953/check-if-bigdecimal-is-integer-value
        return decimalValue.signum() == 0 || decimalValue.scale() <= 0 || decimalValue.stripTrailingZeros().scale() <= 0;
    }

    // static class
    private NumberUtils() {}
}
