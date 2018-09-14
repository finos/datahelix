package com.scottlogic.deg.generator.generation.field_value_sources;

import com.scottlogic.deg.generator.utils.IRandomNumberGenerator;

import java.util.Iterator;

public class MaximumValuesFieldValueSource implements IFieldValueSource {

    private final IFieldValueSource innerSource;
    private final long limit = 10_000_000L;

    public MaximumValuesFieldValueSource(IFieldValueSource innerSource) {

        this.innerSource = innerSource;
    }

    @Override
    public boolean isFinite() {
        return innerSource.isFinite();
    }

    @Override
    public long getValueCount() {
        return innerSource.getValueCount();
    }

    @Override
    public Iterable<Object> generateInterestingValues() {
        return new MaxCountIterable<>(innerSource.generateInterestingValues(), limit);
    }

    @Override
    public Iterable<Object> generateAllValues() {
        return new MaxCountIterable<>(innerSource.generateAllValues(), limit);
    }

    @Override
    public Iterable<Object> generateRandomValues(IRandomNumberGenerator randomNumberGenerator) {
        return new MaxCountIterable<>(innerSource.generateRandomValues(randomNumberGenerator), limit);
    }

    public class MaxCountIterable<T> implements Iterable<T> {
        private final Iterable<T> underlyingIterable;
        private long maxNumberOfItemsToReturn;

        public MaxCountIterable(
                Iterable<T> underlyingIterable,
                long maxNumberOfItemsToReturn) {

            this.underlyingIterable = underlyingIterable;
            this.maxNumberOfItemsToReturn = maxNumberOfItemsToReturn;
        }

        @Override
        public Iterator<T> iterator() {
            return new InternalIterator(underlyingIterable.iterator(), maxNumberOfItemsToReturn);
        }


        private class InternalIterator implements Iterator<T> {
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
                T nextValue = this.underlyingIterator.next();
                this.numberOfItemsReturned++;

                if (this.numberOfItemsReturned > maxNumberOfItemsToReturn) {
                    throw new IllegalStateException("Max number of results generated (" + maxNumberOfItemsToReturn + ")");
                }

                return nextValue;
            }
        }
    }
}
