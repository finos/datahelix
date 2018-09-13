package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.utils.NumberUtils;

import java.math.BigDecimal;

/**
 * Granularity expressions could be interpreted differently depending on other constraints on a field (eg, type constraints),
 * so we store all possible parsings in this class, ready to make a GranularityRestrictions object
 * */
public class ParsedGranularity {
    private final BigDecimal numericGranularity;

    public ParsedGranularity(BigDecimal numericGranularity) {
        this.numericGranularity = numericGranularity;
    }

    public static ParsedGranularity parse(Object granularityExpression) {
        if (granularityExpression instanceof Number) {
            BigDecimal asNumber = NumberUtils.coerceToBigDecimal(granularityExpression);

            if (asNumber.compareTo(BigDecimal.ONE) > 0) {
                throw new IllegalArgumentException("Numeric granularity must be less than 1");
            }

            if (!asNumber.equals(BigDecimal.ONE.scaleByPowerOfTen(-asNumber.scale()))) {
                throw new IllegalArgumentException("Numeric granularity must be fractional power of ten");
            }

            return new ParsedGranularity(asNumber);
        }

        throw new IllegalArgumentException("Can't interpret granularity expression: " + granularityExpression);
    }

    public BigDecimal getNumericGranularity() {
        return this.numericGranularity;
    }
}
