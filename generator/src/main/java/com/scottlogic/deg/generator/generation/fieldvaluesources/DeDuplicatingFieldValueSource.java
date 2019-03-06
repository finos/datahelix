package com.scottlogic.deg.generator.generation.fieldvaluesources;

import com.scottlogic.deg.generator.generation.ObservableIterable;
import com.scottlogic.deg.generator.utils.RandomNumberGenerator;

import java.util.Iterator;
import java.util.function.Supplier;


/**
 * A FieldValueSource that will emit all values from originalSource and only those
 * from mustContainValues that have not been emitted from originalSource
 */
public class DeDuplicatingFieldValueSource implements FieldValueSource {
    private final FieldValueSource originalSource;
    private final MustContainsValues mustContainValues;

    public DeDuplicatingFieldValueSource(FieldValueSource originalSource, MustContainsValues mustContainValues) {
        this.originalSource = originalSource;
        this.mustContainValues = mustContainValues;
    }

    @Override
    public boolean isFinite() {
        return originalSource.isFinite();
    }

    @Override
    public long getValueCount() {
        //may exceed the actual number of values emitted if duplicates are encountered
        return originalSource.getValueCount() + mustContainValues.getValueCount();
    }

    @Override
    public Iterable<Object> generateInterestingValues() {
        ObservableIterable<Object> objects = new ObservableIterable<>(originalSource.generateInterestingValues());
        objects.addObserver((source, value) -> mustContainValues.removeValue(value));

        return new AppendingIterable<>(objects, mustContainValues::generateAllValues);
    }

    @Override
    public Iterable<Object> generateAllValues() {
        ObservableIterable<Object> objects = new ObservableIterable<>(originalSource.generateAllValues());
        objects.addObserver((source, value) -> mustContainValues.removeValue(value));

        return new AppendingIterable<>(objects, mustContainValues::generateAllValues);
    }

    @Override
    public Iterable<Object> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
        ObservableIterable<Object> objects = new ObservableIterable<>(
            originalSource.generateRandomValues(randomNumberGenerator));
        objects.addObserver((source, value) -> mustContainValues.removeValue(value));

        return new AppendingIterable<>(objects, () -> mustContainValues.generateRandomValues(randomNumberGenerator));
    }

    /**
     * An iterable that can append values to it, by way of a Supplier function
     * @param <T> The type of item
     */
    private class AppendingIterable<T> implements Iterable<T>{

        private final Iterable<T> iterable;
        private final Supplier<Iterable<T>> getIteratorToAppend;

        AppendingIterable(Iterable<T> iterable, Supplier<Iterable<T>> getIteratorToAppend) {
            this.iterable = iterable;
            this.getIteratorToAppend = getIteratorToAppend;
        }

        @Override
        public Iterator<T> iterator() {
            return new AppendingIterator<T>(iterable.iterator(), () -> getIteratorToAppend.get().iterator());
        }
    }

    /**
     * An iterator which will emit all values from iterator and values in the iterator supplied
     * by getIteratorToAppend.
     *
     * The Supplier function will be called in a lazy-fashion once the first iterator has been
     * exhausted, allowing for its contents to change whilst the first iterator is processed
     * @param <T> The type of item being iterated over
     */
    private class AppendingIterator<T> implements Iterator<T>{
        private Supplier<Iterator<T>> getIteratorToAppend;
        private Iterator<T> iteratorToUse;

        AppendingIterator(Iterator<T> iterator, Supplier<Iterator<T>> getIteratorToAppend) {
            this.iteratorToUse = iterator;
            this.getIteratorToAppend = getIteratorToAppend;
        }

        @Override
        public boolean hasNext() {
            if (!iteratorToUse.hasNext() && getIteratorToAppend != null){
                iteratorToUse = getIteratorToAppend.get();
                getIteratorToAppend = null;
            }

            return iteratorToUse.hasNext();
        }

        @Override
        public T next() {
            return iteratorToUse.next();
        }
    }
}
