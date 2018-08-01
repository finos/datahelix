package com.scottlogic.deg.generator.generation.tmpReducerOutput;

import java.math.BigDecimal;

public class NumericRestrictions {
    public NumericLimit min;
    public NumericLimit max;

    public static class NumericLimit {
        private final BigDecimal limit;
        private final boolean inclusive;

        public NumericLimit(BigDecimal limit, boolean inclusive) {
            this.limit = limit;
            this.inclusive = inclusive;
        }

        public BigDecimal getLimit() {
            return limit;
        }

        public boolean getInclusive() {
            return inclusive;
        }
    }
}
