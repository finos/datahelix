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

import com.scottlogic.deg.generator.fieldspecs.whitelist.ElementFrequency;
import com.scottlogic.deg.generator.fieldspecs.whitelist.FrequencyWhitelist;
import com.scottlogic.deg.generator.fieldspecs.whitelist.Whitelist;
import com.scottlogic.deg.generator.utils.RandomNumberGenerator;
import com.scottlogic.deg.generator.utils.SupplierBasedIterator;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CannedValuesFieldValueSource implements FieldValueSource {
    private final Whitelist<Object> allValues;
    private final Whitelist<Object> interestingValues;

    public CannedValuesFieldValueSource(Whitelist<Object> values) {
        this.allValues = values;
        this.interestingValues = values;
    }

    public static FieldValueSource of(ElementFrequency<Object>... values) {
        Set<ElementFrequency<Object>> set = Arrays.stream(values).collect(Collectors.toSet());
        return new CannedValuesFieldValueSource(new FrequencyWhitelist<Object>(set));
    }

    @Override
    public boolean isFinite() {
        return true;
    }

    @Override
    public long getValueCount() {
        return allValues.set().size();
    }

    @Override
    public Stream<Object> generateInterestingValues() {
        return interestingValues.set().stream();
    }

    @Override
    public Stream<Object> generateAllValues() {
        return allValues.set().stream();
    }

    @Override
    public Stream<Object> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
        return allValues.generate(randomNumberGenerator);
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
