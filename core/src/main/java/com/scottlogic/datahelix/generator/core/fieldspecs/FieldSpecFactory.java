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

import com.scottlogic.datahelix.generator.common.profile.FieldType;
import com.scottlogic.datahelix.generator.common.profile.InSetRecord;
import com.scottlogic.datahelix.generator.common.util.Defaults;
import com.scottlogic.datahelix.generator.common.whitelist.DistributedList;
import com.scottlogic.datahelix.generator.common.whitelist.WeightedElement;
import com.scottlogic.datahelix.generator.core.generation.fieldvaluesources.FieldValueSource;
import com.scottlogic.datahelix.generator.core.restrictions.TypedRestrictions;
import com.scottlogic.datahelix.generator.core.restrictions.bool.BooleanRestrictions;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.scottlogic.datahelix.generator.core.restrictions.linear.LinearRestrictionsFactory.*;
import static com.scottlogic.datahelix.generator.core.restrictions.string.StringRestrictionsFactory.forMaxLength;
import static java.util.Collections.singletonList;

public class FieldSpecFactory {
    private static final NullOnlyFieldSpec NULL_ONLY_FIELD_SPEC = new NullOnlyFieldSpec();

    private FieldSpecFactory() {
        throw new IllegalArgumentException("Should not instantiate factory");
    }

    public static WhitelistFieldSpec fromAllowedList(Collection<Object> allowedList) {
        return fromList(DistributedList.uniform(allowedList));
    }

    public static WhitelistFieldSpec fromInSetRecords(List<InSetRecord> inSetRecords) {
        DistributedList<Object> distributedList = new DistributedList<>(inSetRecords.stream()
            .map(v -> new WeightedElement<>(v.getElement(), v.getWeightValueOrDefault()))
            .collect(Collectors.toList()));

        return fromList(distributedList);
    }

    public static WhitelistFieldSpec fromAllowedSingleValue(Object value) {
        return fromAllowedList(singletonList(value));
    }

    public static WhitelistFieldSpec fromList(DistributedList<Object> whitelist) {
        return new WhitelistFieldSpec(whitelist, true);
    }

    public static RestrictionsFieldSpec fromRestriction(TypedRestrictions restrictions) {
        return new RestrictionsFieldSpec(restrictions, true, Collections.emptySet());
    }

    public static RestrictionsFieldSpec fromType(FieldType type) {
        switch (type) {
            case NUMERIC:
                return new RestrictionsFieldSpec(createDefaultNumericRestrictions(), true, Collections.emptySet());
            case DATETIME:
                return new RestrictionsFieldSpec(createDefaultDateTimeRestrictions(), true, Collections.emptySet());
            case TIME:
                return new RestrictionsFieldSpec(createDefaultTimeRestrictions(), true, Collections.emptySet());
            case STRING:
                return new RestrictionsFieldSpec(forMaxLength(Defaults.MAX_STRING_LENGTH), true, Collections.emptySet());
            case BOOLEAN:
                return new RestrictionsFieldSpec(new BooleanRestrictions(), true, Collections.emptySet());
            default:
                throw new IllegalArgumentException("Unable to create FieldSpec from type " + type.name());
        }
    }

    public static NullOnlyFieldSpec nullOnly() {
        return NULL_ONLY_FIELD_SPEC;
    }

    public static GeneratorFieldSpec fromGenerator(FieldValueSource fieldValueSource, Predicate<Object> setValueAcceptFunction){
        return new GeneratorFieldSpec(fieldValueSource, setValueAcceptFunction, true);
    }
}
