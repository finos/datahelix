package com.scottlogic.deg.generator.restrictions;

import java.time.LocalDateTime;

public class DateTimeRestrictions {
    public DateTimeLimit min;
    public DateTimeLimit max;

    public static class DateTimeLimit {
        private final LocalDateTime limit;
        private final boolean inclusive;

        public DateTimeLimit(LocalDateTime limit, boolean inclusive) {
            this.limit = limit;
            this.inclusive = inclusive;
        }

        public LocalDateTime getLimit() {
            return limit;
        }

        public boolean isInclusive() {
            return inclusive;
        }
    }
}
