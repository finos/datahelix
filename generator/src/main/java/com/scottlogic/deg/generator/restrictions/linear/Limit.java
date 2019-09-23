package com.scottlogic.deg.generator.restrictions.linear;

import java.util.Objects;

public class Limit<T extends Comparable<? super T>> {

    private final T limit;
    private final boolean isInclusive;

    public Limit(T limit, boolean isInclusive) {
        this.limit = limit;
        this.isInclusive = isInclusive;
    }

    public T getValue() {
        return limit;
    }

    public boolean isInclusive() {
        return isInclusive;
    }

    public boolean isBefore(T other) {
        if (isInclusive){
            return limit.compareTo(other) <= 0;
        }
        return limit.compareTo(other) < 0;
    }

    public boolean isAfter(T other) {
        if (isInclusive){
            return limit.compareTo(other) >= 0;
        }
        return limit.compareTo(other) > 0;
    }

    public String toString() {
        return String.format(
            "%s%s",
            limit.toString(),
            isInclusive ? " inclusive" : ""
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Limit<?> limit1 = (Limit<?>) o;
        return isInclusive == limit1.isInclusive &&
            Objects.equals(limit, limit1.limit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(limit, isInclusive);
    }
}
