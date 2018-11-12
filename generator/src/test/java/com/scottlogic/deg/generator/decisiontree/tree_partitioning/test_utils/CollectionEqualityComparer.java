package com.scottlogic.deg.generator.decisiontree.tree_partitioning.test_utils;

import java.util.ArrayList;
import java.util.Collection;

public interface CollectionEqualityComparer<T> {
    ArrayList<T> getItemsMissingFrom(Collection x, Collection y);
}
