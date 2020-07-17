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

package com.scottlogic.datahelix.generator.profile.validators.profile.constraints.relations;

import com.scottlogic.datahelix.generator.common.profile.FieldType;
import com.scottlogic.datahelix.generator.common.validators.ValidationResult;
import com.scottlogic.datahelix.generator.profile.dtos.FieldDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.relations.RelationalConstraintDTO;
import com.scottlogic.datahelix.generator.profile.validators.profile.FieldValidator;

import java.util.List;
import java.util.Optional;

import static com.scottlogic.datahelix.generator.common.validators.ValidationResult.quote;

public class TemporalRelationalConstraintValidator<T extends RelationalConstraintDTO> extends RelationalConstraintValidator<T>
{
    public TemporalRelationalConstraintValidator(List<FieldDTO> fields)
    {
        super(fields);
    }

    @Override
    public ValidationResult validate(T dto)
    {
        return ValidationResult.combine(
            fieldMustBeValid(dto, dto.field, FIELD_DESCRIPTION),
            fieldMustBeValid(dto, dto.getOtherField(), RELATED_FIELD_DESCRIPTION),
            fieldTypeMustBeTemporal(dto, dto.field, FIELD_DESCRIPTION),
            fieldTypeMustBeTemporal(dto, dto.getOtherField(), RELATED_FIELD_DESCRIPTION),
            offsetMustBeValid(dto)
        );
    }

    private ValidationResult fieldTypeMustBeTemporal(T dto, String fieldName, String fieldDescription)
    {
        Optional<FieldType> fieldType = findField(fieldName)
            .map(f -> FieldValidator.getSpecificFieldType(f).getFieldType());

        return !fieldType.isPresent() || fieldType.get() == FieldType.DATETIME || fieldType.get() == FieldType.TIME
            ? ValidationResult.success()
            : ValidationResult.failure(String.format("%s must be time/datetime however it has type %s%s", fieldDescription, quote(fieldType.get()), getErrorInfo(dto)));
    }
}
