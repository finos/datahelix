package com.scottlogic.deg.generator.restrictions.linear;

import java.time.OffsetDateTime;
import java.util.Objects;

public class DateTimeLimit implements Limit<OffsetDateTime> {
    private final OffsetDateTime limit;
    private final boolean isInclusive;

    public DateTimeLimit(OffsetDateTime limit, boolean isInclusive) {
        this.limit = limit;
        this.isInclusive = isInclusive;
    }
    
    @Override
    public OffsetDateTime getValue() {
        return limit;
    }
    
    public boolean isInclusive() {
        return isInclusive;
    }

    @Override
    public boolean isBefore(OffsetDateTime other) {
        if (isInclusive){
            return limit.compareTo(other) <= 0;
        }
        return limit.compareTo(other) < 0;
    }

    @Override
    public boolean isAfter(OffsetDateTime other) {
        if (isInclusive){
            return limit.compareTo(other) >= 0;
        }
        return limit.compareTo(other) > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateTimeLimit that = (DateTimeLimit) o;
        return isInclusive == that.isInclusive &&
            Objects.equals(limit, that.limit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(limit, isInclusive);
    }

    @Override
    public String toString() {
        return String.format("%s%s",limit, isInclusive ? " inclusive" : "");
    }
}
