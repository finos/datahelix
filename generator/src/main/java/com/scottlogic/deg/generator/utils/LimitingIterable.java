package com.scottlogic.deg.generator.utils;

import java.util.Iterator;

public class LimitingIterable<T> implements Iterable<T> {
    private final Iterable<T> underlyingIterable;
    private long maxNumberOfItemsToReturn;

    public LimitingIterable(
        Iterable<T> underlyingIterable,
        long maxNumberOfItemsToReturn) {

        this.underlyingIterable = underlyingIterable;
        this.maxNumberOfItemsToReturn = maxNumberOfItemsToReturn;
    }

    @Override
    public Iterator<T> iterator() {
        return new InternalIterator(underlyingIterable.iterator(), maxNumberOfItemsToReturn);
    }


    private class InternalIterator implements Iterator<T>
    {
        private final Iterator<T> underlyingIterator;
        private final long maxNumberOfItemsToReturn;
        private long numberOfItemsReturned = 0;

        private InternalIterator(
            Iterator<T> underlyingIterator,
            long maxNumberOfItemsToReturn) {

            this.underlyingIterator = underlyingIterator;
            this.maxNumberOfItemsToReturn = maxNumberOfItemsToReturn;
        }

        @Override
        public boolean hasNext() {
            return this.numberOfItemsReturned < this.maxNumberOfItemsToReturn
                && underlyingIterator.hasNext();
        }

        @Override
        public T next() {
            T nextValue = this.underlyingIterator.next();
            this.numberOfItemsReturned++;
            return nextValue;
        }
    }
}
