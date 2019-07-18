package com.scottlogic.deg.generator.fieldspecs.whitelist;

import java.util.Collections;
import java.util.Set;

public class NullDistributedSet<T> implements DistributedSet<T> {

    private final Set<WeightedElement<T>> underlyingSet;

    public NullDistributedSet() {
        underlyingSet = Collections.unmodifiableSet(Collections.singleton(WeightedElement.ofNull()));
    }

    @Override
    public Set<WeightedElement<T>> distributedSet() {
        return underlyingSet;
    }

    @Override
    public T pick(double randomValue) {
        return null;
    }
}
