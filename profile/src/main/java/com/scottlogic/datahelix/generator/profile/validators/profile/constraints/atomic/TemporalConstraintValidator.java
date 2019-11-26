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

package com.scottlogic.datahelix.generator.profile.validators.profile.constraints.atomic;


import com.scottlogic.datahelix.generator.common.profile.FieldType;
import com.scottlogic.datahelix.generator.common.util.defaults.DateTimeDefaults;
import com.scottlogic.datahelix.generator.common.validators.ValidationResult;
import com.scottlogic.datahelix.generator.profile.dtos.FieldDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.temporal.TemporalConstraintDTO;
import com.scottlogic.datahelix.generator.profile.factories.DateTimeFactory;
import com.scottlogic.datahelix.generator.profile.factories.TimeFactory;

import java.time.OffsetDateTime;
import java.util.List;

public class TemporalConstraintValidator extends AtomicConstraintValidator<TemporalConstraintDTO> {
    public TemporalConstraintValidator(List<FieldDTO> fields) {
        super(fields);
    }

    @Override
    public final ValidationResult validate(TemporalConstraintDTO dto) {
        ValidationResult fieldMustBeValid = fieldMustBeValid(dto);
        if (!fieldMustBeValid.isSuccess) return fieldMustBeValid;

        FieldType fieldType = fields.stream().filter(f -> f.name.equals(dto.field)).findFirst().get().type.getFieldType();

        ValidationResult validationResult;

        switch (fieldType){
            case DATETIME:
                validationResult = dateTimeMustBeValid(dto);
                break;
            case TIME:
                validationResult = timeMustBeValid(dto);
                break;
            default:
                throw new IllegalStateException("Unexpected Temporal constraint type: " + fieldType);
        }

        if (!validationResult.isSuccess) return validationResult;

        return fieldTypeMustMatchValueType(dto, fieldType);
    }

    private ValidationResult dateTimeMustBeValid(TemporalConstraintDTO dto) {
        String dateTime = dto.getDate();
        if (dateTime == null || dateTime.isEmpty()) {
            return ValidationResult.failure("DateTime must be specified" + getErrorInfo(dto));
        }
        try {
            OffsetDateTime offsetDateTime = DateTimeFactory.create(dateTime);
            OffsetDateTime max = DateTimeDefaults.get().max();
            OffsetDateTime min = DateTimeDefaults.get().min();
            if (offsetDateTime.compareTo(max) > 0 || offsetDateTime.compareTo(min) < 0) {
                return ValidationResult.failure("Dates must be between 0001-01-01T00:00:00.000Z and 9999-12-31T23:59:59.999Z" + getErrorInfo(dto));
            }
            return ValidationResult.success();
        } catch (Exception e) {
            return ValidationResult.failure(e.getMessage() + getErrorInfo(dto));
        }
    }

    private ValidationResult timeMustBeValid(TemporalConstraintDTO dto) {
        String time = dto.getDate();
        if (time == null || time.isEmpty()) {
            return ValidationResult.failure("Time must be specified" + getErrorInfo(dto));
        }
        try {
            TimeFactory.create(time);
            return ValidationResult.success();
        } catch (Exception e) {
            return ValidationResult.failure(e.getMessage() + getErrorInfo(dto));
        }
    }
}
