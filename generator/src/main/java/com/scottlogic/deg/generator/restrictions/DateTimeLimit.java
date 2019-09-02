package com.scottlogic.deg.generator.restrictions;

import java.time.OffsetDateTime;
import java.util.Objects;

public class DateTimeLimit {
    private final OffsetDateTime limit;

    @Override
    public String toString() {
        return String.format("%s%s",limit, inclusive ? " inclusive" : "");
    }

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

    private OffsetDateTime getReferenceTime(int nanoOffset) {
        if (inclusive) {
            return limit;
        }

        return limit.plusNanos(nanoOffset);
    }
}
