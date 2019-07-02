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

package com.scottlogic.deg.generator.utils;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class RandomMergingIterable<E> implements Iterable<E> {
    private final List<Iterable<E>> iterableList;
    private final RandomNumberGenerator randomNumberGenerator;

    public RandomMergingIterable(List<Iterable<E>> iterableList, RandomNumberGenerator randomNumberGenerator) {
        this.iterableList = iterableList;
        this.randomNumberGenerator = randomNumberGenerator;
    }

    @Override
    public Iterator<E> iterator() {
        final List<Iterator<E>> iteratorList = iterableList.stream()
                .map(Iterable::iterator)
                .collect(Collectors.toList());
        return new InternalIterator(iteratorList, randomNumberGenerator);
    }

    private class InternalIterator implements Iterator<E> {

        private final List<Iterator<E>> iteratorList;
        private final RandomNumberGenerator randomNumberGenerator;

        private InternalIterator(List<Iterator<E>> iteratorList, RandomNumberGenerator randomNumberGenerator) {
            this.iteratorList = iteratorList;
            this.randomNumberGenerator = randomNumberGenerator;
        }

        @Override
        public boolean hasNext() {
            return iteratorList.stream().anyMatch(Iterator::hasNext);
        }

        @Override
        public E next() {
            List<Iterator<E>> nonEmptyIteratorsList = iteratorList
                .stream()
                .filter(iterator -> iterator.hasNext())
                .collect(Collectors.toList());

            return nonEmptyIteratorsList
                .get(randomNumberGenerator.nextInt(nonEmptyIteratorsList.size()))
                .next();
        }
    }
}
