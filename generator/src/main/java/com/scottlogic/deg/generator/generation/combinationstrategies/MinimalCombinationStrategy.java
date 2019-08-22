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

import java.util.*;
import java.util.stream.*;

public class MinimalCombinationStrategy implements CombinationStrategy {

    @Override
    public Stream<DataBag> permute(Stream<DataBagStream> dataBagSequences) {
        List<DataBagIterable> iterators = dataBagSequences
            .map(DataBagStream::toIterator)
            .collect(Collectors.toList());

        return iterators.stream().allMatch(dataBagIterable -> dataBagIterable.iterator().hasNext())
            ? StreamSupport.stream(iterable(iterators).spliterator(), false)
            : Stream.empty();
    }

    private Iterable<DataBag> iterable(List<DataBagIterable> iterators) {
        return () -> new InternalIterator(iterators);
    }

    class InternalIterator implements Iterator<DataBag> {
        private final List<DataBagIterable> iterators;
        private final Map<Iterator<DataBag>, DataBag> lastValues;

        InternalIterator(List<DataBagIterable> iterators) {
            this.iterators = iterators;
            this.lastValues = new HashMap<>();
        }

        @Override
        public boolean hasNext() {
            boolean uniqueHaveNext =
                or(iterators.stream()
                        .filter(DataBagIterable::isUnique)
                        .allMatch(dataBagIterable -> dataBagIterable.iterator().hasNext()),
                    iterators.stream()
                        .noneMatch(DataBagIterable::isUnique));

            boolean notUniqueHaveNext =
                or(iterators.stream()
                        .filter(dataBagIterable -> !dataBagIterable.isUnique())
                        .anyMatch(dataBagIterable -> dataBagIterable.iterator().hasNext()),
                    iterators.stream()
                        .noneMatch(dataBagIterable -> !dataBagIterable.isUnique()));

            return uniqueHaveNext && notUniqueHaveNext;
        }

        private boolean or(boolean a, boolean b) {
            return a || b;
        }

        @Override
        public DataBag next() {
            iterators
                .stream()
                .filter(dataBagIterable -> dataBagIterable.iterator().hasNext())
                .forEach(dataBagIterable -> lastValues.put(dataBagIterable.iterator(), dataBagIterable.iterator().next()));

            return DataBag.merge(lastValues.values().toArray(new DataBag[0]));
        }
    }
}
