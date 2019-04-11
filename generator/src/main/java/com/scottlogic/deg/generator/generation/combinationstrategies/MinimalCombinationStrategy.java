package com.scottlogic.deg.generator.generation.combinationstrategies;

import com.scottlogic.deg.generator.generation.rows.Row;
import com.scottlogic.deg.generator.generation.rows.RowMerger;

import java.util.*;
import java.util.stream.*;

public class MinimalCombinationStrategy implements CombinationStrategy {

    @Override
    public Stream<Row> permute(Stream<Stream<Row>> rowSequences) {
        List<Iterator<Row>> iterators = rowSequences
                .map(BaseStream::iterator)
                .collect(Collectors.toList());

        return iterators.stream().allMatch(Iterator::hasNext)
            ? StreamSupport.stream(iterable(iterators).spliterator(), false)
            : Stream.empty();
    }

    private Iterable<Row> iterable(List<Iterator<Row>> iterators) {
        return () -> new InternalIterator(iterators);
    }

    class InternalIterator implements Iterator<Row> {
        private final List<Iterator<Row>> iterators;
        private final Map<Iterator<Row>, Row> lastValues;

        InternalIterator(List<Iterator<Row>> iterators) {
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
        public Row next() {
            iterators
                .stream()
                .filter(Iterator::hasNext)
                .forEach(iterator -> lastValues.put(iterator, iterator.next()));

            return RowMerger.merge(lastValues.values().toArray(new Row[0]));
        }
    }
}
