package com.scottlogic.deg.generator.generation.fieldvaluesources;

import com.scottlogic.deg.generator.generation.ObservableIterable;
import com.scottlogic.deg.generator.utils.RandomNumberGenerator;

import java.util.Iterator;
import java.util.function.Supplier;

public class DeDuplicatingFieldValueSource implements FieldValueSource {
    private final FieldValueSource originalSource;
    private final MustContainsFieldValueSource requiredValueSource;

    public DeDuplicatingFieldValueSource(FieldValueSource originalSource, MustContainsFieldValueSource requiredValueSource) {
        this.originalSource = originalSource;
        this.requiredValueSource = requiredValueSource;
    }

    @Override
    public boolean isFinite() {
        return originalSource.isFinite();
    }

    @Override
    public long getValueCount() {
        //may exceed the actual number of values emitted if duplicates are encountered
        return originalSource.getValueCount() + requiredValueSource.getValueCount();
    }

    @Override
    public Iterable<Object> generateInterestingValues() {
        ObservableIterable<Object> objects = new ObservableIterable<>(originalSource.generateInterestingValues());
        objects.addObserver((source, value) -> requiredValueSource.removeValue(value));

        return new AppendingIterable<>(objects, requiredValueSource::generateAllValues);
    }

    @Override
    public Iterable<Object> generateAllValues() {
        ObservableIterable<Object> objects = new ObservableIterable<>(originalSource.generateAllValues());
        objects.addObserver((source, value) -> requiredValueSource.removeValue(value));

        return new AppendingIterable<>(objects, requiredValueSource::generateAllValues);
    }

    @Override
    public Iterable<Object> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
        ObservableIterable<Object> objects = new ObservableIterable<>(
            originalSource.generateRandomValues(randomNumberGenerator));
        objects.addObserver((source, value) -> requiredValueSource.removeValue(value));

        return new AppendingIterable<>(objects, requiredValueSource::generateAllValues);
    }

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
