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

import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.generator.utils.RandomNumberGenerator;

import java.util.Objects;
import java.util.stream.Stream;

public class CannedValuesFieldValueSource implements FieldValueSource {
    private final DistributedList<Object> allValues;

    public CannedValuesFieldValueSource(DistributedList<Object> values) {
        this.allValues = values;
    }

    @Override
    public Stream<Object> generateInterestingValues() {
        return generateAllValues();
    }

    @Override
    public Stream<Object> generateAllValues() {
        return allValues.stream();
    }

    @Override
    public Stream<Object> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
        return Stream.generate(() -> pickFromDistribution(randomNumberGenerator));
    }

    private Object pickFromDistribution(RandomNumberGenerator random) {
        return allValues.pickRandomly(random);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        CannedValuesFieldValueSource otherSource = (CannedValuesFieldValueSource) obj;
        return allValues.equals(otherSource.allValues);
    }

    @Override
    public int hashCode() {
        return Objects.hash(allValues);
    }
}
