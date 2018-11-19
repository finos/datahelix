package com.scottlogic.deg.generator.decisiontree.test_utils;

public interface EqualityComparer {
    int getHashCode(Object item);
    boolean equals(Object item1, Object item2);
}
