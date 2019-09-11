package com.scottlogic.deg.generator.restrictions.linear;

import java.math.BigDecimal;

public class NumericConverter implements Converter<BigDecimal> {
    @Override
    public BigDecimal convert(Object value) {
        return new BigDecimal(value.toString());
    }

    @Override
    public boolean isCorrectType(Object value) {
        return value instanceof Number;
    }
}
