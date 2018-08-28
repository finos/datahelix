package com.scottlogic.deg.generator.utils;

import java.util.Iterator;
import java.util.function.Predicate;

public class FilteringIterator<T> implements Iterator<T> {
    private final Iterator<T> underlyingIterator;

    private T nextValueToReturn;
    private boolean haveCompletedIteration;

    private Predicate<T> predicate;

    public FilteringIterator(Iterator<T> underlyingIterator, Predicate<T> predicate) {
        this.underlyingIterator = underlyingIterator;
        this.predicate = predicate;

        this.searchForNextValidValue();
    }

    private void searchForNextValidValue() {
        while (this.underlyingIterator.hasNext()) {
            T nextValue = this.underlyingIterator.next();
            if (this.predicate.test(nextValue)) {
                this.nextValueToReturn = nextValue;
                return;
            }
        }

        this.haveCompletedIteration = true;
    }

    @Override
    public boolean hasNext() {
        return !this.haveCompletedIteration;
    }

    @Override
    public T next() {
        T valueToReturn = this.nextValueToReturn;
        this.searchForNextValidValue();
        return valueToReturn;
    }
}
