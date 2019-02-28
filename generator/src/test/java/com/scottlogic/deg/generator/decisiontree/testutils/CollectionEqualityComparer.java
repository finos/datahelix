package com.scottlogic.deg.generator.decisiontree.testutils;

import java.util.Collection;

public interface CollectionEqualityComparer<T> {
    Collection<T> getItemsMissingFrom(Collection x, Collection y);
}
