package com.scottlogic.deg.generator.decisiontree.tree_partitioning.test_utils;

import java.util.Collection;

public interface CollectionEqualityComparer<T> {
    Collection<T> getItemsMissingFrom(Collection x, Collection y);
}
