package com.scottlogic.datahelix.generator.common.whitelist;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class UniformList<T> extends DistributedList<T>{

    public UniformList(Collection<T> underlyingWeights) {
        super(underlyingWeights.stream().map(WeightedElement::withDefaultWeight).collect(Collectors.toList()));
    }

    public static <T> UniformList<T> singleton(final T element) {
        return new UniformList(Collections.singleton(element));
    }

    @Override
    public boolean isDistributedList() {
        return true;
    }
}
