package com.scottlogic.deg.generator.restrictions.linear;

import com.scottlogic.deg.common.util.Defaults;

import java.time.OffsetDateTime;
import java.util.Objects;

public class DateTimeLimit implements Limit<OffsetDateTime> {
    private final OffsetDateTime limit;
    private final boolean isInclusive;

    public DateTimeLimit(OffsetDateTime limit, boolean isInclusive) {
        this.limit = capLimit(limit);
        this.isInclusive = isInclusive;
    }

    private OffsetDateTime capLimit(OffsetDateTime dateTime) {
        if (dateTime.isAfter(Defaults.ISO_MAX_DATE)) {
            return Defaults.ISO_MAX_DATE;
        } else if (dateTime.isBefore(Defaults.ISO_MIN_DATE)){
         return Defaults.ISO_MIN_DATE;
        }
        return dateTime;
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
