package com.scottlogic.deg.generator.generation.field_value_sources;

import com.scottlogic.deg.generator.utils.ConcatenatingIterable;
import com.scottlogic.deg.generator.utils.IRandomNumberGenerator;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class CombiningFieldValueSource implements IFieldValueSource {
    private final List<IFieldValueSource> underlyingSources;

    public CombiningFieldValueSource(List<IFieldValueSource> underlyingSources) {
        this.underlyingSources = underlyingSources;
    }

    @Override
    public boolean isFinite() {
        return underlyingSources.stream().allMatch(IFieldValueSource::isFinite);
    }

    @Override
    public long getValueCount() {
        return underlyingSources.stream()
            .map(IFieldValueSource::getValueCount)
            .reduce(Long::sum)
            .get();
    }

    @Override
    public Iterable<Object> generateInterestingValues() {
        return new ConcatenatingIterable<>(
                underlyingSources.stream()
                        .map(IFieldValueSource::generateInterestingValues)
                        .collect(Collectors.toList()));
    }

    @Override
    public Iterable<Object> generateAllValues() {
        return new ConcatenatingIterable<>(
            underlyingSources.stream()
                .map(IFieldValueSource::generateAllValues)
                .collect(Collectors.toList()));
    }

    @Override
    public Iterable<Object> generateRandomValues(IRandomNumberGenerator randomNumberGenerator) {
        return () -> new InternalRandomIterator(
            underlyingSources.stream()
                .map(source -> source.generateRandomValues(randomNumberGenerator).iterator())
                .collect(Collectors.toList()),
            randomNumberGenerator);
    }

    private class InternalRandomIterator implements Iterator<Object> {
        private final List<Iterator<Object>> iterators;
        private final IRandomNumberGenerator randomNumberGenerator;

        InternalRandomIterator(
            List<Iterator<Object>> iterators,
            IRandomNumberGenerator randomNumberGenerator) {

            this.iterators = iterators.stream()
                .filter(Iterator::hasNext)
                .collect(Collectors.toList());

            this.randomNumberGenerator = randomNumberGenerator;
        }

        @Override
        public boolean hasNext() {
            return !this.iterators.isEmpty();
        }

        @Override
        public Object next() {
            int iteratorIndex = randomNumberGenerator.nextInt(
                underlyingSources.size());

            Iterator<Object> iterator = iterators.get(iteratorIndex);

            Object value = iterator.next();

            if (!iterator.hasNext())
                this.iterators.remove(iteratorIndex);

            return value;
        }
    }
}
