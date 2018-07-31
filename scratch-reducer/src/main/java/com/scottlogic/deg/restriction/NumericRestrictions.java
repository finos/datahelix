package com.scottlogic.deg.restriction;

public class NumericRestrictions<T extends Number> {
    private final Class<T> typeToken;

    public T min;
    public T max;

    public NumericRestrictions(Class<T> typeToken) {
        this.typeToken = typeToken;
    }

    public Class<T> getTypeToken() {
        return typeToken;
    }
}
