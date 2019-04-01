package com.scottlogic.deg.generator.generation.combinationstrategies;

import com.scottlogic.deg.generator.generation.databags.GeneratedObject;

import java.util.*;
import java.util.stream.*;

public class MinimalCombinationStrategy implements CombinationStrategy {

    @Override
    public Stream<GeneratedObject> permute(Stream<Stream<GeneratedObject>> dataBagSequences) {
        List<Iterator<GeneratedObject>> iterators = dataBagSequences
                .map(BaseStream::iterator)
                .collect(Collectors.toList());

        return iterators.stream().allMatch(Iterator::hasNext)
            ? StreamSupport.stream(iterable(iterators).spliterator(), false)
            : Stream.empty();
    }

    private Iterable<GeneratedObject> iterable(List<Iterator<GeneratedObject>> iterators) {
        return () -> new InternalIterator(iterators);
    }

    class InternalIterator implements Iterator<GeneratedObject> {
        private final List<Iterator<GeneratedObject>> iterators;
        private final Map<Iterator<GeneratedObject>, GeneratedObject> lastValues;

        InternalIterator(List<Iterator<GeneratedObject>> iterators) {
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
        public GeneratedObject next() {
            iterators
                .stream()
                .filter(Iterator::hasNext)
                .forEach(iterator -> lastValues.put(iterator, iterator.next()));

            return GeneratedObject.merge(lastValues.values().toArray(new GeneratedObject[0]));
        }
    }
}
