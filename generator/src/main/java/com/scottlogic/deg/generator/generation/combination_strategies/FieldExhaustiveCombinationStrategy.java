package com.scottlogic.deg.generator.generation.combination_strategies;

import com.scottlogic.deg.generator.generation.DataBag;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FieldExhaustiveCombinationStrategy implements ICombinationStrategy {

    @Override
    public Iterable<DataBag> permute(List<Iterable<DataBag>> dataBagSequences) {
        return new FieldExhaustiveCombinationStrategy.InternalIterable(dataBagSequences);
    }

    class InternalIterable implements Iterable<DataBag> {
        private final List<Iterable<DataBag>> dataRowSequences;

        InternalIterable(List<Iterable<DataBag>> dataRowSequences) {
            this.dataRowSequences = dataRowSequences;
        }

        @Override
        public Iterator<DataBag> iterator() {
            return new FieldExhaustiveCombinationStrategy.InternalIterator(dataRowSequences);
        }
    }

    class InternalIterator implements Iterator<DataBag> {
        private int indexOfSequenceToVary;

        private final DataBag[] baselines;
        private final List<Iterable<DataBag>> dataRowSequences;
        private final List<Iterator<DataBag>> sequenceIterators;

        InternalIterator(List<Iterable<DataBag>> dataRowSequences) {
            this.baselines = new DataBag[dataRowSequences.size()];

            this.sequenceIterators = dataRowSequences.stream()
                .map(Iterable::iterator)
                .collect(Collectors.toList());

            this.indexOfSequenceToVary = 0;

            this.dataRowSequences = dataRowSequences;
        }

        private DataBag getBaseline(int seqIndex) {
            if (baselines[seqIndex] != null)
                return baselines[seqIndex];

            DataBag baseline = sequenceIterators.get(seqIndex).next();

            baselines[seqIndex] = baseline;

            return baseline;
        }

        @Override
        public boolean hasNext() {
            // kind of inefficient
            return sequenceIterators.stream().anyMatch(Iterator::hasNext);
        }

        @Override
        public DataBag next() {
            return IntStream.range(0, dataRowSequences.size())
                .mapToObj(seqIndex -> {
                    if (seqIndex != indexOfSequenceToVary) {
                        return this.getBaseline(seqIndex);
                    }
                    else {
                        if (!sequenceIterators.get(seqIndex).hasNext()) {
                            this.indexOfSequenceToVary++;
                            return this.getBaseline(seqIndex);
                        }
                        return sequenceIterators.get(seqIndex).next();
                    }
                })
                .reduce(new DataBag(), (db1, db2) -> DataBag.merge(db1, db2));
        }
    }
}
