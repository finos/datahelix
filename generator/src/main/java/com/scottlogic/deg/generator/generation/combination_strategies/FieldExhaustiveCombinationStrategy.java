package com.scottlogic.deg.generator.generation.combination_strategies;

import com.scottlogic.deg.generator.generation.databags.DataBag;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FieldExhaustiveCombinationStrategy implements ICombinationStrategy {

    @Override
    public Iterable<DataBag> permute(Stream<Iterable<DataBag>> dataBagSequences) {
        return new FieldExhaustiveCombinationStrategy.InternalIterable(dataBagSequences);
    }

    class InternalIterable implements Iterable<DataBag> {
        private final Stream<Iterable<DataBag>> dataBagSequences;

        InternalIterable(Stream<Iterable<DataBag>> dataBagSequences) {
            this.dataBagSequences = dataBagSequences;
        }

        @Override
        public Iterator<DataBag> iterator() {
            List<SequenceAndBaselineTuple> tuples = this.dataBagSequences
                    .map(Iterable::iterator)
                    .map(iterator -> new SequenceAndBaselineTuple(iterator))
                    .collect(Collectors.toList());

            if (tuples.stream().anyMatch(t -> t.baseline == null))
                return Collections.emptyIterator();

            return new InternalIterator(tuples);
        }
    }

    class SequenceAndBaselineTuple {
        private Iterator<DataBag> iterator;
        private DataBag baseline;

        public SequenceAndBaselineTuple(Iterator<DataBag> iterator) {
            this.iterator = iterator;
            this.baseline = iterator.hasNext() ? iterator.next() : null;
        }

        public DataBag next(){
            return iterator.next();
        }

        public boolean hasNext(){
            return iterator.hasNext();
        }
    }

    class InternalIterator implements Iterator<DataBag> {
        private final List<SequenceAndBaselineTuple> tuples;

        private Integer indexOfSequenceToVary;

        InternalIterator(List<SequenceAndBaselineTuple> tuples) {
            this.tuples = tuples;
            this.indexOfSequenceToVary = null;
        }

        @Override
        public boolean hasNext() {
            if (this.indexOfSequenceToVary == null)
                return true; // because this means we haven't output a baselines row yet. I know this code is awful, I'll definitely fix it -MH

            // kind of inefficient
            return this.tuples.stream().anyMatch(tuple -> tuple.hasNext());
        }

        @Override
        public DataBag next() {
            if (this.indexOfSequenceToVary == null) {
                this.indexOfSequenceToVary = 0;

                return this.tuples.stream()
                        .map(tuple -> tuple.baseline)
                    .reduce(DataBag.empty, (db1, db2) -> DataBag.merge(db1, db2));
            }

            return IntStream.range(0, this.tuples.size())
                .mapToObj(seqIndex -> {
                    SequenceAndBaselineTuple tuple = this.tuples.get(seqIndex);

                    if (seqIndex != this.indexOfSequenceToVary)
                        return tuple.baseline;

                    if (!tuple.hasNext()) {
                        this.indexOfSequenceToVary++;
                        return tuple.baseline;
                    }
                    return tuple.next();
                })
                .reduce(DataBag.empty, (db1, db2) -> DataBag.merge(db1, db2));
        }
    }
}
