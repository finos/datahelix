/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottlogic.deg.generator.generation.combinationstrategies;

import com.scottlogic.deg.generator.generation.databags.*;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class PinningCombinationStrategy implements CombinationStrategy {

    @Override
    public Stream<DataBag> permute(Stream<DataBagStream> dataBagSequences) {
        Iterable<DataBag> iterable = new PinningCombinationStrategy
                .InternalIterable(dataBagSequences);

        return StreamSupport.stream(iterable.spliterator(), false);
    }

    class InternalIterable implements Iterable<DataBag> {
        private final Stream<DataBagStream> dataBagSequences;

        InternalIterable(Stream<DataBagStream> dataBagSequences) {
            this.dataBagSequences = dataBagSequences;
        }

        @Override
        public Iterator<DataBag> iterator() {
            List<SequenceAndBaselineTuple> tuples = this.dataBagSequences
                    .map(sequence -> new SequenceAndBaselineTuple(sequence.toIterator()))
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
