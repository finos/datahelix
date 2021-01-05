/*
 * Copyright 2019-2021 Scott Logic Ltd
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

package com.scottlogic.datahelix.generator.core.fieldspecs;

import com.scottlogic.datahelix.generator.common.distribution.DistributedList;
import com.scottlogic.datahelix.generator.core.generation.fieldvaluesources.CannedValuesFieldValueSource;
import com.scottlogic.datahelix.generator.core.generation.fieldvaluesources.FieldValueSource;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WeightedLegalValuesFieldSpec extends FieldSpec implements ValuesFieldSpec {
    private final DistributedList<Object> legalValues;

    WeightedLegalValuesFieldSpec(DistributedList<Object> legalValues, boolean nullable) {
        super(nullable);
        if (legalValues.isEmpty()){
            throw new UnsupportedOperationException("cannot create with empty legalValues");
        }
        this.legalValues = legalValues;
    }

    @Override
    public boolean canCombineWithLegalValue(Object value) {
        return legalValues.list().contains(value);
    }

    @Override
    public FieldValueSource getFieldValueSource() {
        return appendNullSource(new CannedValuesFieldValueSource(legalValues));
    }

    @Override
    public Optional<FieldSpec> merge(FieldSpec other, boolean useFinestGranularityAvailable) {
        final boolean notNullable = !isNullable() || !other.isNullable();

        if (other instanceof WeightedLegalValuesFieldSpec) {
            return Optional.empty();
        }

        final DistributedList<Object> newSet = new DistributedList<>(
            getLegalValues().distributedList().stream()
                .filter(holder -> other.canCombineWithLegalValue(holder.element()))
                .distinct()
                .collect(Collectors.toList()));

        if (newSet.isEmpty() && notNullable) {
            return Optional.empty();
        }

        final FieldSpec newSpec = newSet.isEmpty() ? FieldSpecFactory.nullOnly() : FieldSpecFactory.fromList(newSet);
        return Optional.of(notNullable ? newSpec.withNotNull() : newSpec);
    }

    @Override
    public WeightedLegalValuesFieldSpec withNotNull() {
        return new WeightedLegalValuesFieldSpec(legalValues, false);
    }

    public DistributedList<Object> getLegalValues() {
        return legalValues;
    }

    @Override
    public FieldSpec withMappedValues(Function<Object, Object> parse) {
        DistributedList<Object> allowedValuesList = new DistributedList<>(legalValues.distributedList().stream()
            .map(value -> value.withMappedValue(parse)).collect(Collectors.toList()));

        return new WeightedLegalValuesFieldSpec(allowedValuesList, nullable);
    }

    @Override
    public String toString() {
        if (legalValues.isEmpty()) {
            return "Null only";
        }
        return (nullable ? "" : "Not Null ") + String.format("IN %s", legalValues);
    }

    public int hashCode() {
        return Objects.hash(nullable, legalValues);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        WeightedLegalValuesFieldSpec other = (WeightedLegalValuesFieldSpec) obj;
        return Objects.equals(nullable, other.nullable)
            && Objects.equals(legalValues, other.legalValues);
    }
}
