package com.scottlogic.deg.generator.restrictions;

import java.time.OffsetDateTime;
import java.util.Objects;

public class DateTimeRestrictions {
    public DateTimeLimit min;
    public DateTimeLimit max;

    public static boolean isDateTime(Object o){
        return o instanceof OffsetDateTime;
    }

    public boolean match(Object o) {
        if(!DateTimeRestrictions.isDateTime(o)){
            return false;
        }

        OffsetDateTime d = (OffsetDateTime) o;

        if(min != null){
            if(d.compareTo(min.getLimit()) < (min.isInclusive() ? 0 : 1))
            {
                return false;
            }
        }

        if(max != null){
            if(d.compareTo(max.getLimit()) > (max.isInclusive() ? 0 : -1))
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateTimeRestrictions that = (DateTimeRestrictions) o;
        return Objects.equals(min, that.min) &&
            Objects.equals(max, that.max);
    }

    @Override
    public int hashCode() {
        return Objects.hash(min, max);
    }

    public static class DateTimeLimit {
        private final OffsetDateTime limit;
        private final boolean inclusive;

        public DateTimeLimit(OffsetDateTime limit, boolean inclusive) {
            this.limit = limit;
            this.inclusive = inclusive;
        }

        public OffsetDateTime getLimit() {
            return limit;
        }

        public boolean isInclusive() {
            return inclusive;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DateTimeLimit that = (DateTimeLimit) o;
            return inclusive == that.inclusive &&
                Objects.equals(limit, that.limit);
        }

        @Override
        public int hashCode() {
            return Objects.hash(limit, inclusive);
        }

        public boolean isAfter(DateTimeLimit max) {
            OffsetDateTime minLimit = getReferenceTime(1);
            OffsetDateTime maxLimit = max.getReferenceTime(-1);

            return minLimit.isAfter(maxLimit);
        }

        private OffsetDateTime getReferenceTime(int nanoOffset){
            if (inclusive){
                return limit;
            }

            return limit.plusNanos(nanoOffset);
        }
    }
}
