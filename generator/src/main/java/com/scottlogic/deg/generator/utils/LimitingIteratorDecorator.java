package com.scottlogic.deg.generator.utils;

import java.util.Iterator;

public class LimitingIteratorDecorator<T> implements Iterator<T>
{
    private final Iterator<T> underlyingIterator;
    private final int maxNumberOfItemsToReturn;
    private int numberOfItemsReturned = 0;

    public LimitingIteratorDecorator(
        Iterator<T> underlyingIterator,
        int maxNumberOfItemsToReturn) {

        this.underlyingIterator = underlyingIterator;
        this.maxNumberOfItemsToReturn = maxNumberOfItemsToReturn;
    }

    @Override
    public boolean hasNext() {
        return this.numberOfItemsReturned < this.maxNumberOfItemsToReturn;
    }

    @Override
    public T next() {
        T nextValue = this.underlyingIterator.next();
        this.numberOfItemsReturned++;
        return nextValue;
    }
}
