package com.scottlogic.deg.generator.decisiontree.test_utils;

import java.util.Collection;

public interface CollectionEqualityComparer<T> {
    Collection<T> getItemsMissingFrom(Collection x, Collection y);
}
