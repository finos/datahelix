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

package com.scottlogic.deg.generator.generation.fieldvaluesources;

import com.scottlogic.deg.common.util.FlatMappingSpliterator;
import com.scottlogic.deg.generator.utils.RandomNumberGenerator;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.scottlogic.deg.common.util.FlatMappingSpliterator.flatMap;
import static com.scottlogic.deg.generator.utils.SetUtils.stream;

public class CombiningFieldValueSource implements FieldValueSource {
    private final List<FieldValueSource> underlyingSources;

    public CombiningFieldValueSource(List<FieldValueSource> underlyingSources) {
        this.underlyingSources = underlyingSources;
    }

    @Override
    public Stream<Object> generateInterestingValues() {
        return flatMap(
            underlyingSources.stream().map(FieldValueSource::generateInterestingValues),
            Function.identity());
    }

    @Override
    public Stream<Object> generateAllValues() {
        return flatMap(
            underlyingSources.stream().map(FieldValueSource::generateAllValues),
            Function.identity());
    }

    @Override
    public Stream<Object> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
        return stream(new InternalRandomIterator(
            underlyingSources.stream()
                .map(source -> source.generateRandomValues(randomNumberGenerator).iterator())
                .collect(Collectors.toList()),
            randomNumberGenerator));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        CombiningFieldValueSource otherSource = (CombiningFieldValueSource) obj;
        return underlyingSources.equals(otherSource.underlyingSources);
    }

    @Override
    public int hashCode() {
        return Objects.hash(underlyingSources);
    }

    private class InternalRandomIterator implements Iterator<Object> {
        private final List<Iterator<Object>> iterators;
        private final RandomNumberGenerator randomNumberGenerator;

        InternalRandomIterator(
            List<Iterator<Object>> iterators,
            RandomNumberGenerator randomNumberGenerator) {

            this.iterators = iterators.stream()
                .filter(Iterator::hasNext)
                .collect(Collectors.toList());

            this.randomNumberGenerator = randomNumberGenerator;
        }

        @Override
        public boolean hasNext() {
            return !this.iterators.isEmpty();
        }

        @Override
        public Object next() {
            int iteratorIndex = randomNumberGenerator.nextInt(
                iterators.size());

            Iterator<Object> iterator = iterators.get(iteratorIndex);

            Object value = iterator.next();

            if (!iterator.hasNext())
                this.iterators.remove(iteratorIndex);

            return value;
        }
    }
}
