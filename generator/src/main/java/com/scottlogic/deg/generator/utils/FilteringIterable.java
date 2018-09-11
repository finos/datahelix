package com.scottlogic.deg.generator.utils;

import java.util.Iterator;
import java.util.function.Predicate;

public class FilteringIterable<T> implements Iterable<T> {
    private final Iterable<T> underlyingIterable;
    private final Predicate<T> predicate;

    public FilteringIterable(Iterable<T> underlyingIterable, Predicate<T> predicate) {
        this.underlyingIterable = underlyingIterable;
        this.predicate = predicate;
    }

    @Override
    public Iterator<T> iterator() {
        return new FilteringIterator<>(underlyingIterable.iterator(), predicate);
    }
}
