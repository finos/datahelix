package com.scottlogic.deg.generator.fieldspecs.whitelist;

import java.util.Set;

public interface DistributedSet<T> {

    Set<T> set();

    Set<WeightedElement<T>> distributedSet();
}
