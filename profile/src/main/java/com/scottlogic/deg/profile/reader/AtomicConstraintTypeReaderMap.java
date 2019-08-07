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

package com.scottlogic.deg.profile.reader;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraints.atomic.*;
import com.scottlogic.deg.common.profile.constraints.delayed.IsAfterDynamicDateTimeConstraint;
import com.scottlogic.deg.common.util.Defaults;
import com.scottlogic.deg.generator.fieldspecs.whitelist.FrequencyDistributedSet;
import com.scottlogic.deg.profile.reader.constraintreaders.SetReader;
import com.scottlogic.deg.profile.reader.constraintreaders.GranularToReader;
import com.scottlogic.deg.profile.reader.constraintreaders.OfTypeReader;
import com.scottlogic.deg.profile.v0_1.AtomicConstraintType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.scottlogic.deg.profile.reader.ConstraintReaderHelpers.getValidatedValue;
import static com.scottlogic.deg.profile.v0_1.AtomicConstraintType.*;

public class AtomicConstraintTypeReaderMap {

    private final String fromFilePath;

    public AtomicConstraintTypeReaderMap(final String fromFilePath) {
        this.fromFilePath = fromFilePath;
    }

    public Map<AtomicConstraintType, ConstraintReader> getConstraintReaderMapEntries() {
        BigDecimal maxStringLength = BigDecimal.valueOf(Defaults.MAX_STRING_LENGTH);

        Map<AtomicConstraintType, ConstraintReader> map = new HashMap<>();

        map.put(IS_OF_TYPE, new OfTypeReader());
        map.put(IS_GRANULAR_TO, new GranularToReader());
        map.put(IS_IN_SET, new SetReader(fromFilePath));

        map.put(FORMATTED_AS,
            (dto, fields) -> new FormatConstraint(
                fields.getByName(dto.field),
                getValidatedValue(dto, String.class)));

        map.put(IS_EQUAL_TO_CONSTANT,
            (dto, fields) -> new EqualToConstraint(
                fields.getByName(dto.field),
                getValidatedValue(dto)));

        map.put(CONTAINS_REGEX,
            (dto, fields) ->
                new ContainsRegexConstraint(
                    fields.getByName(dto.field),
                    Pattern.compile(getValidatedValue(dto, String.class))));

        map.put(MATCHES_REGEX,
            (dto, fields) ->
                new MatchesRegexConstraint(
                    fields.getByName(dto.field),
                    Pattern.compile(getValidatedValue(dto, String.class))));

        map.put(IS_GREATER_THAN_CONSTANT,
            (dto, fields) ->
                new IsGreaterThanConstantConstraint(
                    fields.getByName(dto.field),
                    getValidatedValue(dto, Number.class)));

        map.put(IS_GREATER_THAN_OR_EQUAL_TO_CONSTANT,
            (dto, fields) ->
                new IsGreaterThanOrEqualToConstantConstraint(
                    fields.getByName(dto.field),
                    getValidatedValue(dto, Number.class)));

        map.put(IS_LESS_THAN_CONSTANT,
            (dto, fields) ->
                new IsLessThanConstantConstraint(
                    fields.getByName(dto.field),
                    getValidatedValue(dto, Number.class)));

        map.put(IS_LESS_THAN_OR_EQUAL_TO_CONSTANT,
            (dto, fields) ->
                new IsLessThanOrEqualToConstantConstraint(
                    fields.getByName(dto.field),
                    getValidatedValue(dto, Number.class)));

        map.put(IS_BEFORE_CONSTANT_DATE_TIME,
            (dto, fields) ->
                new IsBeforeConstantDateTimeConstraint(
                    fields.getByName(dto.field),
                    getValidatedValue(dto, OffsetDateTime.class)));

        map.put(IS_BEFORE_OR_EQUAL_TO_CONSTANT_DATE_TIME,
            (dto, fields) ->
                new IsBeforeOrEqualToConstantDateTimeConstraint(
                    fields.getByName(dto.field),
                    getValidatedValue(dto, OffsetDateTime.class)));

        map.put(IS_AFTER_CONSTANT_DATE_TIME,
            (dto, fields) -> {
                Optional<OffsetDateTime> dateValidatedValue =
                    ConstraintReaderHelpers.tryGetValidatedValue(dto, OffsetDateTime.class);

                if (dateValidatedValue.isPresent()) {
                    return new IsAfterConstantDateTimeConstraint(fields.getByName(dto.field), dateValidatedValue.get());
                }

                String otherFieldName = ConstraintReaderHelpers.getValueAsString(dto);
                Field otherField = fields.getByName(otherFieldName);

                return new IsAfterDynamicDateTimeConstraint(
                    new IsAfterConstantDateTimeConstraint(
                        fields.getByName(dto.field),
                        OffsetDateTime.MAX
                    ),
                    otherField
                );
            });

        map.put(IS_AFTER_OR_EQUAL_TO_CONSTANT_DATE_TIME,
            (dto, fields) ->
                new IsAfterOrEqualToConstantDateTimeConstraint(
                    fields.getByName(dto.field),
                    getValidatedValue(dto, OffsetDateTime.class)));

        map.put(IS_NULL,
            (dto, fields) -> new IsNullConstraint(fields.getByName(dto.field)));

        map.put(IS_STRING_LONGER_THAN,
            (dto, fields) ->
                new IsStringLongerThanConstraint(
                    fields.getByName(dto.field),
                    ConstraintReaderHelpers.ensureValueBetween(
                        dto,
                        Integer.class,
                        BigDecimal.ZERO,
                        maxStringLength.subtract(BigDecimal.ONE))));

        map.put(IS_STRING_SHORTER_THAN,
            (dto, fields) ->
                new IsStringShorterThanConstraint(
                    fields.getByName(dto.field),
                    ConstraintReaderHelpers.ensureValueBetween(
                        dto,
                        Integer.class,
                        BigDecimal.ONE,
                        maxStringLength.add(BigDecimal.ONE))));

        map.put(HAS_LENGTH,
            (dto, fields) ->
                new StringHasLengthConstraint(
                    fields.getByName(dto.field),
                    ConstraintReaderHelpers.ensureValueBetween(
                        dto,
                        Integer.class,
                        BigDecimal.ZERO,
                        maxStringLength)));

        return map;
    }


}
