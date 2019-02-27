package com.scottlogic.deg.generator.decisiontree.testutils;

public interface EqualityComparer {
    int getHashCode(Object item);
    boolean equals(Object item1, Object item2);
}
