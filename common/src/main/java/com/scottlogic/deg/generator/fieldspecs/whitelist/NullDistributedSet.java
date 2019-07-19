package com.scottlogic.deg.generator.fieldspecs.whitelist;

import com.scottlogic.deg.generator.utils.RandomNumberGenerator;

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
    public T pick(RandomNumberGenerator randomValue) {
        return null;
    }
}
