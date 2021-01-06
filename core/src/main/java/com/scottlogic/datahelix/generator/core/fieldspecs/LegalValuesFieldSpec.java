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

package com.scottlogic.datahelix.generator.core.fieldspecs;

import com.scottlogic.datahelix.generator.common.distribution.DistributedList;
import com.scottlogic.datahelix.generator.core.generation.fieldvaluesources.CannedValuesFieldValueSource;
import com.scottlogic.datahelix.generator.core.generation.fieldvaluesources.FieldValueSource;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LegalValuesFieldSpec extends FieldSpec implements ValuesFieldSpec {
    private final List<Object> legalValues;

    LegalValuesFieldSpec(List<Object> legalValues, boolean nullable) {
        super(nullable);
        if (legalValues.isEmpty()){
            throw new UnsupportedOperationException("cannot create with empty legalValue");
        }
        this.legalValues = Collections.unmodifiableList(legalValues);
    }

    @Override
    public boolean canCombineWithLegalValue(Object value) {
        return legalValues.contains(value);
    }

    @Override
    public FieldValueSource getFieldValueSource() {
        return appendNullSource(new CannedValuesFieldValueSource(DistributedList.uniform(legalValues)));
    }

    @Override
    public Optional<FieldSpec> merge(FieldSpec other, boolean useFinestGranularityAvailable) {
        final boolean notNullable = !isNullable() || !other.isNullable();

        final List<Object> newSet = other instanceof LegalValuesFieldSpec
            ? mergeSets((LegalValuesFieldSpec) other)
            : combineSetWithRestrictions(other);

        if (newSet.isEmpty() && notNullable) {
            return Optional.empty();
        }

        final FieldSpec newSpec = newSet.isEmpty() ? FieldSpecFactory.nullOnly() : FieldSpecFactory.fromLegalValuesList(newSet);
        return Optional.of(notNullable ? newSpec.withNotNull() : newSpec);
    }

    private List<Object> mergeSets(LegalValuesFieldSpec other) {
        List<Object> newSet;
        newSet = legalValues.stream()
            .flatMap(leftHolder -> other.legalValues.stream()
                .filter(leftHolder::equals)
            )
            .distinct()
            .collect(Collectors.toList());
        return newSet;
    }

    private List<Object> combineSetWithRestrictions(FieldSpec other) {
        return legalValues.stream()
                .filter(other::canCombineWithLegalValue)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public LegalValuesFieldSpec withNotNull() {
        return new LegalValuesFieldSpec(legalValues, false);
    }

    @Override
    public FieldSpec withMappedValues(Function<Object, Object> parse) {
        List<Object> allowedValuesList = legalValues.stream().map(parse).collect(Collectors.toList());

        return new LegalValuesFieldSpec(allowedValuesList, nullable);
    }

    @Override
    public String toString() {
        if (legalValues.isEmpty()) {
            return "Null only";
        }
        return (nullable ? "" : "Not Null ") + String.format("IN %s", legalValues);
    }

    public int hashCode() {
        return Objects.hash(legalValues, nullable);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        LegalValuesFieldSpec other = (LegalValuesFieldSpec) obj;
        return Objects.equals(legalValues, other.legalValues)
            && Objects.equals(nullable, other.nullable);
    }
}
