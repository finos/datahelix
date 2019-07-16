package com.scottlogic.deg.generator.fieldspecs.whitelist;

import java.util.Collections;
import java.util.Set;

public class NullDistributedSet<T> implements DistributedSet<T> {

    @Override
    public Set<T> set() {
        return Collections.singleton(null);
    }

    @Override
    public Set<WeightedElement<T>> distributedSet() {
        return Collections.singleton(new WeightedElement<>(null, 1.0D));
    }

    @Override
    public T pickFromDistribution(double randomValue) {
        return null;
    }
}
