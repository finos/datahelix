package com.scottlogic.deg.generator.utils;

import java.util.Iterator;

public class CyclicIterable<T> implements Iterable<T> {
    private final Iterable<T> underlyingIterable;

    public CyclicIterable(Iterable<T> underlyingIterable) {
        this.underlyingIterable = underlyingIterable;
    }

    @Override
    public Iterator<T> iterator() {
        return new InternalIterator(this.underlyingIterable);
    }

    class InternalIterator implements Iterator<T> {
        private final Iterable<T> sourceIterable;
        private Iterator<T> currentIterator;

        InternalIterator(Iterable<T> sourceIterable) {
            this.sourceIterable = sourceIterable;
            this.currentIterator = sourceIterable.iterator();
        }

        @Override
        public boolean hasNext() {
            return currentIterator.hasNext();
        }

        @Override
        public T next() {
            final T next = currentIterator.next();
            if (!currentIterator.hasNext()) {
                currentIterator = sourceIterable.iterator();
            }
            return next;
        }
    }
}
