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
package com.scottlogic.deg.profile.validators.profile.constraints.atomic;


import com.scottlogic.deg.common.profile.FieldType;
import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.profile.dtos.FieldDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.temporal.TemporalConstraintDTO;
import com.scottlogic.deg.profile.factories.DateTimeFactory;

import java.time.OffsetDateTime;
import java.util.List;

public class TemporalConstraintValidator extends AtomicConstraintValidator<TemporalConstraintDTO>
{

    public TemporalConstraintValidator(String rule, List<FieldDTO> fields)
    {
        super(rule, fields);
    }

    @Override
    public final ValidationResult validate(TemporalConstraintDTO dto)
    {
        ValidationResult fieldMustBeValid = fieldMustBeValid(dto);
        if (!fieldMustBeValid.isSuccess) return fieldMustBeValid;

        ValidationResult dateTimeMustBeValid = dateTimeMustBeValid(dto);
        if (!dateTimeMustBeValid.isSuccess) return dateTimeMustBeValid;

        return fieldTypeMustMatchValueType(dto, FieldType.DATETIME);
    }

    private ValidationResult dateTimeMustBeValid(TemporalConstraintDTO dto)
    {
        String dateTime = dto.getDate();
        if (dateTime == null || dateTime.isEmpty())
        {
            return ValidationResult.failure("DateTime must be specified" + getErrorInfo(dto));
        }
        try
        {
            OffsetDateTime offsetDateTime = DateTimeFactory.create(dateTime);
            if (offsetDateTime.getYear() > 9999 || offsetDateTime.getYear() < 1)
            {
                return ValidationResult.failure("Dates must be between 0001-01-01T00:00:00.000Z and 9999-12-31T23:59:59.999Z" + getErrorInfo(dto));
            }
            return ValidationResult.success();
        } catch (Exception e)
        {
            return ValidationResult.failure(e.getMessage() + getErrorInfo(dto));
        }
    }
}
