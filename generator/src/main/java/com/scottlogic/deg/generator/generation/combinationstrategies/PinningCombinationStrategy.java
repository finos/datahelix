package com.scottlogic.deg.generator.generation.combinationstrategies;

import com.scottlogic.deg.generator.generation.rows.Row;
import com.scottlogic.deg.generator.generation.rows.GeneratedObjectMerger;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class PinningCombinationStrategy implements CombinationStrategy {

    @Override
    public Stream<Row> permute(Stream<Stream<Row>> dataBagSequences) {
        Iterable<Row> iterable = new PinningCombinationStrategy
                .InternalIterable(dataBagSequences);

        return StreamSupport.stream(iterable.spliterator(), false);
    }

    class InternalIterable implements Iterable<Row> {
        private final Stream<Stream<Row>> dataBagSequences;

        InternalIterable(Stream<Stream<Row>> dataBagSequences) {
            this.dataBagSequences = dataBagSequences;
        }

        @Override
        public Iterator<Row> iterator() {
            List<SequenceAndBaselineTuple> tuples = this.dataBagSequences
                    .map(sequence -> new SequenceAndBaselineTuple(sequence.iterator()))
                    .collect(Collectors.toList());

            if (tuples.stream().anyMatch(t -> t.baseline == null))
                return Collections.emptyIterator();

            return new InternalIterator(tuples);
        }
    }

    class SequenceAndBaselineTuple {
        private Iterator<Row> iterator;
        private Row baseline;

        public SequenceAndBaselineTuple(Iterator<Row> iterator) {
            this.iterator = iterator;
            this.baseline = iterator.hasNext() ? iterator.next() : null;
        }

        public Row next(){
            return iterator.next();
        }

        public boolean hasNext(){
            return iterator.hasNext();
        }
    }

    class InternalIterator implements Iterator<Row> {
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
        public Row next() {
            if (this.indexOfSequenceToVary == null) {
                this.indexOfSequenceToVary = 0;

                return this.tuples.stream()
                        .map(tuple -> tuple.baseline)
                    .reduce(Row.empty, (db1, db2) -> GeneratedObjectMerger.merge(db1, db2));
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
                .reduce(Row.empty, (db1, db2) -> GeneratedObjectMerger.merge(db1, db2));
        }
    }
}
