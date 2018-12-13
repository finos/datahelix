package com.scottlogic.deg.generator.generation.combination_strategies;

import com.scottlogic.deg.generator.generation.databags.DataBag;

import java.util.*;
import java.util.stream.*;

public class MinimalCombinationStrategy implements CombinationStrategy {

    @Override
    public Stream<DataBag> permute(Stream<Stream<DataBag>> dataBagSequences) {
        List<Iterator<DataBag>> iterators = dataBagSequences
                .map(BaseStream::iterator)
                .collect(Collectors.toList());

        return iterators.stream().allMatch(Iterator::hasNext)
            ? StreamSupport.stream(iterable(iterators).spliterator(), false)
            : Stream.empty();
    }

    private Iterable<DataBag> iterable(List<Iterator<DataBag>> iterators) {
        return () -> new InternalIterator(iterators);
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
