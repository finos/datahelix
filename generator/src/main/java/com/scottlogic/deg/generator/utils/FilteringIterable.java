package com.scottlogic.deg.generator.utils;

import java.util.Iterator;
import java.util.function.Predicate;

public class FilteringIterable<T> implements Iterable<T> {
    private final Iterable<T> underlyingIterator;

    private T nextValueToReturn;
    private boolean haveCompletedIteration;

    private Predicate<T> predicate;

    public FilteringIterable(Iterable<T> underlyingIterator, Predicate<T> predicate) {
        this.underlyingIterator = underlyingIterator;
        this.predicate = predicate;
    }
    @Override
    public Iterator<T> iterator() {
        return new FilteringIterator<>(underlyingIterator.iterator(), predicate);
    }
}
