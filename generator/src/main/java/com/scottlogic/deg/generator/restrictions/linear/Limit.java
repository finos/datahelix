package com.scottlogic.deg.generator.restrictions.linear;

public interface Limit<T>{

    T getValue();

    boolean isInclusive();

    boolean isBefore(T other);

    boolean isAfter(T other);
}
