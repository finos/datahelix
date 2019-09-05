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

import com.scottlogic.deg.common.profile.constraints.atomic.*;
import com.scottlogic.deg.common.profile.constraints.delayed.IsAfterDynamicDateConstraint;
import com.scottlogic.deg.common.profile.constraints.delayed.IsBeforeDynamicDateConstraint;
import com.scottlogic.deg.common.util.Defaults;
import com.scottlogic.deg.profile.reader.constraintreaders.*;
import com.scottlogic.deg.profile.dto.AtomicConstraintType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.scottlogic.deg.profile.reader.ConstraintReaderHelpers.*;
import static com.scottlogic.deg.profile.reader.ConstraintReaderHelpers.getValidatedValue;
import static com.scottlogic.deg.profile.dto.AtomicConstraintType.*;

public class AtomicConstraintTypeReaderMap {

    private final String fromFilePath;

    public AtomicConstraintTypeReaderMap(final String fromFilePath) {
        this.fromFilePath = fromFilePath;
    }

    public Map<AtomicConstraintType, AtomicConstraintReader> getConstraintReaderMapEntries() {
        BigDecimal maxStringLength = BigDecimal.valueOf(Defaults.MAX_STRING_LENGTH);

        Map<AtomicConstraintType, AtomicConstraintReader> map = new HashMap<>();

        map = Stream.of(AtomicConstraintType.values())
            .collect(Collectors.toMap(
                type->type,
                type -> new FactoryConstraintReader(type)));

        map.put(IS_GRANULAR_TO, new GranularToReader());
        
        map.putAll(getDelayedMapEntries());

        map.put(IS_UNIQUE, (dto, fields) -> new RemoveFromTree());
        map.put(FORMATTED_AS, (dto, fields) -> new RemoveFromTree());

        return map;
    }

    private Map<AtomicConstraintType, AtomicConstraintReader> getDelayedMapEntries() {
        Map<AtomicConstraintType, AtomicConstraintReader> map = new HashMap<>();

        map.put(IS_EQUAL_TO_FIELD, new EqualToFieldReader());

        map.put(IS_BEFORE_FIELD_DATE_TIME,
            (dto, fields) ->
                new IsBeforeDynamicDateConstraint(
                    new IsBeforeConstantDateTimeConstraint(
                        fields.getByName(dto.field),
                        OffsetDateTime.MIN
                    ),
                    fields.getByName(getValueAsString(dto)),
                    false
                )
        );

        map.put(IS_BEFORE_OR_EQUAL_TO_FIELD_DATE_TIME,
            (dto, fields) ->
                new IsBeforeDynamicDateConstraint(
                    new IsBeforeOrEqualToConstantDateTimeConstraint(
                        fields.getByName(dto.field),
                        OffsetDateTime.MIN
                    ),
                    fields.getByName(getValueAsString(dto)),
                    true
                )
        );

        map.put(IS_AFTER_FIELD_DATE_TIME,
            (dto, fields) ->
                new IsAfterDynamicDateConstraint(
                    new IsAfterConstantDateTimeConstraint(
                        fields.getByName(dto.field),
                        OffsetDateTime.MAX
                    ),
                    fields.getByName(getValueAsString(dto)),
                    false
                ));

        map.put(IS_AFTER_OR_EQUAL_TO_FIELD_DATE_TIME,
            (dto, fields) ->
                new IsAfterDynamicDateConstraint(
                    new IsAfterOrEqualToConstantDateTimeConstraint(
                        fields.getByName(dto.field),
                        OffsetDateTime.MAX
                    ),
                    fields.getByName(getValueAsString(dto)),
                    true
                )
        );

        return map;
    }


}
