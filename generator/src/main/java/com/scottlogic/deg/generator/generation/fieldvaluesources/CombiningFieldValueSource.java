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

import com.scottlogic.deg.generator.utils.ConcatenatingIterable;
import com.scottlogic.deg.generator.utils.RandomNumberGenerator;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CombiningFieldValueSource implements FieldValueSource {
    private final List<FieldValueSource> underlyingSources;

    public CombiningFieldValueSource(List<FieldValueSource> underlyingSources) {
        this.underlyingSources = underlyingSources;
    }

    @Override
    public boolean isFinite() {
        return underlyingSources.stream().allMatch(FieldValueSource::isFinite);
    }

    @Override
    public long getValueCount() {
        return underlyingSources
            .stream()
            .map(FieldValueSource::getValueCount)
            .reduce(Long::sum)
            .orElse(0L);
    }

    @Override
    public Stream<Object> generateInterestingValues() {
        return underlyingSources.stream().flatMap(FieldValueSource::generateInterestingValues);

    }

    @Override
    public Stream<Object> generateAllValues() {
        return underlyingSources.stream().flatMap(FieldValueSource::generateAllValues);

    }

    @Override
    public Stream<Object> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
        return Stream.generate(underlyingSources::stream).flatMap(Function.identity());

/*        return () -> new InternalRandomIterator(
            underlyingSources.stream()
                .map(source -> source.generateRandomValues(randomNumberGenerator).iterator())
                .collect(Collectors.toList()),
            randomNumberGenerator);*/
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        CombiningFieldValueSource otherSource = (CombiningFieldValueSource) obj;
        return underlyingSources.equals(otherSource.underlyingSources);
    }

}
