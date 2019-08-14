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

import com.scottlogic.deg.common.date.ChronoUnitWorkingDayWrapper;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.common.profile.constraints.Constraint;
import com.scottlogic.deg.common.profile.constraints.atomic.EqualToConstraint;
import com.scottlogic.deg.common.profile.constraints.delayed.IsEqualToDynamicDateConstraint;
import com.scottlogic.deg.profile.reader.ConstraintReader;
import com.scottlogic.deg.profile.v0_1.ConstraintDTO;

import java.time.temporal.ChronoUnit;

import static com.scottlogic.deg.profile.reader.ConstraintReaderHelpers.getValidatedValue;
import static com.scottlogic.deg.profile.reader.ConstraintReaderHelpers.getValueAsString;

public class EqualToFieldReader implements ConstraintReader {

    @Override
    public Constraint apply(ConstraintDTO dto, ProfileFields fields) {
        if (dto.offsetUnit != null) {
            ChronoUnitWorkingDayWrapper unit;
            if (((String) dto.offsetUnit).toUpperCase().equals("WORKING DAYS")) {
                unit = new ChronoUnitWorkingDayWrapper(
                    ChronoUnit.valueOf(ChronoUnit.class, ("DAYS").toUpperCase()),
                    true);
            } else {
                unit = new ChronoUnitWorkingDayWrapper(
                    ChronoUnit.valueOf(ChronoUnit.class, ((String) dto.offsetUnit).toUpperCase()),
                    false);
            }

            return new IsEqualToDynamicDateConstraint(
                new EqualToConstraint(fields.getByName(dto.field), getValidatedValue(dto)),
                fields.getByName(getValueAsString(dto)),
                unit,
                dto.offset
            );
        }

        return new IsEqualToDynamicDateConstraint(
            new EqualToConstraint(fields.getByName(dto.field), getValidatedValue(dto)),
            fields.getByName(getValueAsString(dto))
        );
    }
}
