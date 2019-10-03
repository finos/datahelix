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

import com.scottlogic.deg.generator.utils.RandomNumberGenerator;

import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public class NullAppendingValueSource<T> implements FieldValueSource<T> {
    private final FieldValueSource<T> underlyingSource;
    private final Set<T> nullOnly = Collections.singleton(null);

    public NullAppendingValueSource(FieldValueSource<T> underlyingSource) {
        this.underlyingSource = underlyingSource;
    }

    @Override
    public Stream<T> generateInterestingValues() {
        return Stream.concat(
            underlyingSource.generateInterestingValues(),
            nullOnly.stream());
    }

    @Override
    public Stream<T> generateAllValues() {
        return Stream.concat(
            underlyingSource.generateAllValues(),
            nullOnly.stream());
    }

    @Override
    public Stream<T> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
        Iterator<T> randomValues = underlyingSource.generateRandomValues(randomNumberGenerator).iterator();
        return Stream.generate(() -> getNextRandomValue(randomNumberGenerator, randomValues));
    }

    private T getNextRandomValue(RandomNumberGenerator randomNumberGenerator, Iterator<T> randomValues) {
        if (shouldReturnNull(randomNumberGenerator)){
            return null;
        }
        return randomValues.next();
    }

    private boolean shouldReturnNull(RandomNumberGenerator randomNumberGenerator) {
        //1 in 4 chance of getting null
        return randomNumberGenerator.nextInt(4) == 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        NullAppendingValueSource otherSource = (NullAppendingValueSource) obj;
        return underlyingSource.equals(otherSource.underlyingSource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(underlyingSource);
    }
}
