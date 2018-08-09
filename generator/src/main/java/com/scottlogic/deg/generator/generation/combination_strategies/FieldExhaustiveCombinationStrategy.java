package com.scottlogic.deg.generator.generation.combination_strategies;

import com.scottlogic.deg.generator.generation.databags.DataBag;

import java.util.ArrayList;
import java.util.Collections;
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
            List<Iterator<DataBag>> sequenceIterators = dataRowSequences.stream()
                .map(Iterable::iterator)
                .collect(Collectors.toList());

            List<DataBag> baselines = new ArrayList<>(dataRowSequences.size());
            for (int i = 0; i < sequenceIterators.size(); i++) {
                if (!sequenceIterators.get(i).hasNext()) {
                    return Collections.emptyIterator();
                }

                baselines.set(i, sequenceIterators.get(i).next());
            }

            return new InternalIterator(sequenceIterators, baselines);
        }
    }

    class InternalIterator implements Iterator<DataBag> {
        private final List<DataBag> baselinesForInputSequences;
        private final List<Iterator<DataBag>> inputSequencesIterators;

        private Integer indexOfSequenceToVary;

        public InternalIterator(
            List<Iterator<DataBag>> inputSequencesIterators,
            List<DataBag> baselinesForInputSequences) {

            this.baselinesForInputSequences = baselinesForInputSequences;
            this.inputSequencesIterators = inputSequencesIterators;

            this.indexOfSequenceToVary = null;
        }

        @Override
        public boolean hasNext() {
            if (this.indexOfSequenceToVary == null)
                return true; // because this means we haven't output a baselines row yet. I know this code is awful, I'll definitely fix it -MH

            // kind of inefficient
            return this.inputSequencesIterators.stream().anyMatch(Iterator::hasNext);
        }

        @Override
        public DataBag next() {
            if (this.indexOfSequenceToVary == null) {
                this.indexOfSequenceToVary = 0;

                return this.baselinesForInputSequences.stream()
                    .reduce(new DataBag(), (db1, db2) -> DataBag.merge(db1, db2));
            }

            return IntStream.range(0, this.inputSequencesIterators.size())
                .mapToObj(seqIndex -> {
                    if (seqIndex != this.indexOfSequenceToVary) {
                        return this.baselinesForInputSequences.get(seqIndex);
                    }
                    else {
                        if (!this.inputSequencesIterators.get(seqIndex).hasNext()) {
                            this.indexOfSequenceToVary++;
                            return this.baselinesForInputSequences.get(seqIndex);
                        }
                        return this.inputSequencesIterators.get(seqIndex).next();
                    }
                })
                .reduce(new DataBag(), (db1, db2) -> DataBag.merge(db1, db2));
        }
    }
}
