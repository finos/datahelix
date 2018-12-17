package com.scottlogic.deg.generator.utils;

public interface EqualityComparer {
    int getHashCode(Object item);
    boolean equals(Object item1, Object item2);
}
