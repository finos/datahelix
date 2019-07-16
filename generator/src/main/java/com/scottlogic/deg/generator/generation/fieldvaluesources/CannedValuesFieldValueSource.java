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
import com.scottlogic.deg.generator.utils.SupplierBasedIterator;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CannedValuesFieldValueSource implements FieldValueSource {
    private final List<Object> allValues;
    private final List<Object> interestingValues;

    public CannedValuesFieldValueSource(List<Object> values) {
        this.allValues = values;
        this.interestingValues = values;
    }

    public static FieldValueSource of(Object... values) {
        return new CannedValuesFieldValueSource(Arrays.asList(values));
    }

    @Override
    public boolean isFinite() {
        return true;
    }

    @Override
    public long getValueCount() {
        return this.allValues.size();
    }

    @Override
    public Iterable<Object> generateInterestingValues() {
        return this.interestingValues;
    }

    @Override
    public Iterable<Object> generateAllValues() {
        return this.allValues;
    }

    @Override
    public Iterable<Object> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
        return () -> new SupplierBasedIterator<>(
            () -> this.allValues.get(
                randomNumberGenerator.nextInt(
                    this.allValues.size())));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        CannedValuesFieldValueSource otherSource = (CannedValuesFieldValueSource) obj;
        return allValues.equals(otherSource.allValues) && interestingValues.equals(otherSource.interestingValues);
    }

    @Override
    public int hashCode() {
        return Objects.hash(allValues, interestingValues);
    }
}
