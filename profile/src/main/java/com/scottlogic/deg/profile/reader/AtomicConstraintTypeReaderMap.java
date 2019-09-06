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

import static com.scottlogic.deg.profile.dto.AtomicConstraintType.*;

public class AtomicConstraintTypeReaderMap {

    public Map<AtomicConstraintType, AtomicConstraintReader> getDelayedMapEntries() {
        Map<AtomicConstraintType, AtomicConstraintReader> map = new HashMap<>();

        map.put(IS_EQUAL_TO_FIELD, new EqualToFieldReader());

        map.put(IS_BEFORE_FIELD_DATE_TIME,
            (dto, fields) ->
                new IsBeforeDynamicDateConstraint(
                    fields.getByName((String)dto.value),
                    false,
                    fields.getByName(dto.field)
                )
        );

        map.put(IS_BEFORE_OR_EQUAL_TO_FIELD_DATE_TIME,
            (dto, fields) ->
                new IsBeforeDynamicDateConstraint(
                    fields.getByName((String)dto.value),
                    true,
                    fields.getByName(dto.field)
                )
        );

        map.put(IS_AFTER_FIELD_DATE_TIME,
            (dto, fields) ->
                new IsAfterDynamicDateConstraint(
                    fields.getByName((String)dto.value),
                    false,
                    fields.getByName(dto.field)
                ));

        map.put(IS_AFTER_OR_EQUAL_TO_FIELD_DATE_TIME,
            (dto, fields) ->
                new IsAfterDynamicDateConstraint(
                    fields.getByName((String)dto.value),
                    true,
                    fields.getByName(dto.field)
                )
        );

        return map;
    }


}
