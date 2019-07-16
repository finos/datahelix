package com.scottlogic.deg.generator.fieldspecs.whitelist;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class NullDistributedSet<T> implements DistributedSet<T> {

    @Override
    public Set<T> set() {
        return Collections.singleton(null);
    }

    @Override
    public Set<WeightedElement<T>> distributedSet() {
        return set().stream().map(e -> new WeightedElement<>(e, 1.0D)).collect(Collectors.toSet());
    }

    @Override
    public T pickFromDistribution(double randomValue) {
        return null;
    }
}
