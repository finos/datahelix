package com.scottlogic.deg.restriction;

public class NumericRestrictions<T extends Number & Comparable<T>> {
    private final Class<T> typeToken;

    public T min;
    public T max;

    public NumericRestrictions(Class<T> typeToken) {
        this.typeToken = typeToken;
    }

    public Class<T> getTypeToken() {
        return typeToken;
    }

    public NumericRestrictions<T> constructReified() {
        return new NumericRestrictions<>(typeToken);
    }
}
