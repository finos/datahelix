package com.scottlogic.deg.generator.fieldspecs.whitelist;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class NullDistributedSet<T> implements DistributedSet<T> {

    @Override
    public Set<WeightedElement<T>> distributedSet() {
        return Collections.singleton(new WeightedElement<>(null, 1.0D));
    }

    @Override
    public T pick(double randomValue) {
        return null;
    }
}
