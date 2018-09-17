package com.scottlogic.deg.generator.utils;

import java.util.Iterator;

public class HardLimitingIterable<T> implements Iterable<T> {
    private final Iterable<T> underlyingIterable;
    private long maxNumberOfItemsToReturn;

    public HardLimitingIterable(
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
            return underlyingIterator.hasNext();
        }

        @Override
        public T next() {
            if(this.numberOfItemsReturned > maxNumberOfItemsToReturn){
                throw new IllegalStateException("Maximum number of iterations reached.");
            }

            T nextValue = this.underlyingIterator.next();
            this.numberOfItemsReturned++;
            return nextValue;
        }
    }
}
