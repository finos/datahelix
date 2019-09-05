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

package com.scottlogic.deg.profile.reader.constraintreaders;

import com.scottlogic.deg.common.date.TemporalAdjusterGenerator;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.common.profile.constraints.Constraint;
import com.scottlogic.deg.common.profile.constraints.atomic.EqualToConstraint;
import com.scottlogic.deg.common.profile.constraints.delayed.IsEqualToDynamicDateConstraint;
import com.scottlogic.deg.profile.dto.ConstraintDTO;
import com.scottlogic.deg.profile.reader.AtomicConstraintReader;

import java.time.temporal.ChronoUnit;

public class EqualToFieldReader implements AtomicConstraintReader {

    @Override
    public Constraint apply(ConstraintDTO dto, ProfileFields fields) {
        if (dto.offset != null && dto.offsetUnit != null) {
            return createOffsetConstraint(dto, fields);
        }

        return new IsEqualToDynamicDateConstraint(
            new EqualToConstraint(fields.getByName(dto.field), "to be overriden"),
            fields.getByName((String)dto.value)
        );
    }

    private Constraint createOffsetConstraint(ConstraintDTO dto, ProfileFields fields) {
        String offsetUnitUpperCase = dto.offsetUnit.toUpperCase();
        boolean workingDay = offsetUnitUpperCase.equals("WORKING DAYS");
        TemporalAdjusterGenerator unit = new TemporalAdjusterGenerator(
            ChronoUnit.valueOf(ChronoUnit.class, workingDay ? "DAYS" : offsetUnitUpperCase),
            workingDay
        );

        return new IsEqualToDynamicDateConstraint(
            new EqualToConstraint(fields.getByName(dto.field), "to be overriden"),
            fields.getByName((String)dto.value),
            unit,
            dto.offset
        );
    }
}
