package com.scottlogic.deg.generator.utils;

import java.util.Iterator;

public class FilteringIterator<T> implements Iterator<T>
{
    private final Iterator<T> underlyingIterator;

    private T nextValueToReturn;
    private boolean haveCompletedIteration;

    private T valueToFilter;

    public FilteringIterator(Iterator<T> underlyingIterator, T valueToFilter) {
        this.underlyingIterator = underlyingIterator;
        this.valueToFilter = valueToFilter;

        this.searchForNextValidValue();
    }

    private void searchForNextValidValue() {
        while (this.underlyingIterator.hasNext()) {
            T nextValue = this.underlyingIterator.next();
            if (nextValue != this.valueToFilter) {
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
