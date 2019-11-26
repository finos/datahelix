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

package com.scottlogic.datahelix.generator.core.generation.combinationstrategies;

import com.scottlogic.datahelix.generator.core.generation.databags.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;

public class MinimalCombinationStrategy implements CombinationStrategy {
    @Override
    public Stream<DataBag> permute(Stream<Supplier<Stream<DataBag>>> dataBagSequences) {
        List<Iterator<DataBag>> iterators = dataBagSequences
            .map(Supplier::get)
            .map(BaseStream::iterator)
            .collect(Collectors.toList());

        return iterators.stream()
            .allMatch(Iterator::hasNext)
            ? StreamSupport.stream(iterable(iterators).spliterator(), false)
            : Stream.empty();
    }

    private Iterable<DataBag> iterable(List<Iterator<DataBag>> iterators) {
        return () -> new InternalIterator(iterators);
    }

    static class InternalIterator implements Iterator<DataBag> {
        private final List<Iterator<DataBag>> iterators;
        private final Map<Iterator<DataBag>, DataBag> lastValues;

        InternalIterator(List<Iterator<DataBag>> iterators) {
            this.iterators = iterators;
            this.lastValues = new HashMap<>();
        }

        @Override
        public boolean hasNext() {
            return uniqueHasNext() && anyHasNext();
        }

        private boolean uniqueHasNext() {
            return lastValues.entrySet().stream()
                .filter(entry -> entry.getValue().isUnique())
                .allMatch(entry -> entry.getKey().hasNext());
        }

        private boolean anyHasNext() {
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
