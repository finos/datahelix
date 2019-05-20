package com.scottlogic.deg.generator.generation.fieldvaluesources;

import com.scottlogic.deg.generator.utils.ConcatenatingIterable;
import com.scottlogic.deg.generator.utils.RandomNumberGenerator;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CombiningFieldValueSource implements FieldValueSource {
    private final List<FieldValueSource> underlyingSources;

    public CombiningFieldValueSource(List<FieldValueSource> underlyingSources) {
        this.underlyingSources = underlyingSources;
    }

    @Override
    public boolean isFinite() {
        return underlyingSources.stream().allMatch(FieldValueSource::isFinite);
    }

    @Override
    public long getValueCount() {
        return underlyingSources.stream()
            .map(FieldValueSource::getValueCount)
            .reduce(Long::sum)
            .get();
    }

    @Override
    public Iterable<Object> generateInterestingValues() {
        return new ConcatenatingIterable<>(
                underlyingSources.stream()
                        .map(FieldValueSource::generateInterestingValues)
                        .collect(Collectors.toList()));
    }

    @Override
    public Iterable<Object> generateAllValues() {
        return new ConcatenatingIterable<>(
            underlyingSources.stream()
                .map(FieldValueSource::generateAllValues)
                .collect(Collectors.toList()));
    }

    @Override
    public Iterable<Object> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
        return () -> new InternalRandomIterator(
            underlyingSources.stream()
                .map(source -> source.generateRandomValues(randomNumberGenerator).iterator())
                .collect(Collectors.toList()),
            randomNumberGenerator);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        CombiningFieldValueSource otherSource = (CombiningFieldValueSource) obj;
        return underlyingSources.equals(otherSource.underlyingSources);
    }

    @Override
    public int hashCode() {
        return Objects.hash(underlyingSources);
    }

    private class InternalRandomIterator implements Iterator<Object> {
        private final List<Iterator<Object>> iterators;
        private final RandomNumberGenerator randomNumberGenerator;

        InternalRandomIterator(
            List<Iterator<Object>> iterators,
            RandomNumberGenerator randomNumberGenerator) {

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
                iterators.size());

            Iterator<Object> iterator = iterators.get(iteratorIndex);

            Object value = iterator.next();

            if (!iterator.hasNext())
                this.iterators.remove(iteratorIndex);

            return value;
        }
    }
}
