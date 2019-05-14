package com.scottlogic.deg.common;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.ParsePosition;

public class NumberUtils {

    //Copied from: Stack Overflow: https://stackoverflow.com/questions/1078953/check-if-bigdecimal-is-integer-value
    //Author: Joachim Sauer: https://stackoverflow.com/users/40342/joachim-sauer
    public static boolean isInteger(BigDecimal decimalValue) {
        return decimalValue.signum() == 0 || decimalValue.scale() <= 0 || decimalValue.stripTrailingZeros().scale() <= 0;
    }

    public static BigDecimal coerceToBigDecimal( Object value ) {
        // stolen from http://www.java2s.com/Code/Java/Data-Type/ConvertObjecttoBigDecimal.htm
        if (value == null) {
            return null;
        } else if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        } else if (value instanceof String) {
            return tryParse((String) value);
        } else if (value instanceof BigInteger) {
            return new BigDecimal((BigInteger) value);
        } else if (value instanceof Integer) {
            return new BigDecimal((Integer) value);
        } else if (value instanceof Long) {
            return new BigDecimal((Long) value);
        } else if (value instanceof Double) {
            return BigDecimal.valueOf((Double) value);
        } else if (value instanceof Float) {
            return BigDecimal.valueOf((Float) value);
        } else {
            return null;
        }
    }

    public static BigDecimal tryParse(String value) {
        return (BigDecimal) bigDecimalFormatter.parse(value, new ParsePosition(0));
    }

    private static final DecimalFormat bigDecimalFormatter;

    static {
        bigDecimalFormatter = new DecimalFormat();
        bigDecimalFormatter.setParseBigDecimal(true);
    }

    // static class
    private NumberUtils() { }
}
