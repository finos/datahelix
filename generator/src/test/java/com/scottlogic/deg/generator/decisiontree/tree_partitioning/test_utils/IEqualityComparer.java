package com.scottlogic.deg.generator.decisiontree.tree_partitioning.test_utils;

public interface IEqualityComparer {
    int getHashCode(Object item);
    boolean equals(Object item1, Object item2);
}
