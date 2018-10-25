package com.scottlogic.deg.generator.generation.combination_strategies;

import com.scottlogic.deg.generator.generation.databags.DataBag;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MinimalCombinationStrategy implements ICombinationStrategy {

    @Override
    public Iterable<DataBag> permute(List<Iterable<DataBag>> dataBagSequences) {

        return () -> new InternalIterator(
            dataBagSequences
                .stream()
                .map(Iterable::iterator)
                .collect(Collectors.toList()));
    }

    class InternalIterator implements Iterator<DataBag> {
        private final List<Iterator<DataBag>> iterators;
        private final Map<Iterator<DataBag>, DataBag> lastValues;

        InternalIterator(List<Iterator<DataBag>> iterators) {
            this.iterators = iterators;
            this.lastValues = new HashMap<>();
        }

        @Override
        public boolean hasNext() {
            return iterators
                .stream()
                .anyMatch(Iterator::hasNext);
        }

        @Override
        public DataBag next() {
            iterators
                .stream()
                .filter(Iterator::hasNext)
                .forEach(iterator -> lastValues.put(iterator, iterator.next()));

            return DataBag.merge(lastValues.values().toArray(new DataBag[0]));
        }
    }
}
