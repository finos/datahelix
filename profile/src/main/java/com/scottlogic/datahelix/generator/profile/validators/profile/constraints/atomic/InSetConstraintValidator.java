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
import com.scottlogic.datahelix.generator.common.profile.InSetRecord;
import com.scottlogic.datahelix.generator.common.validators.ValidationResult;
import com.scottlogic.datahelix.generator.profile.dtos.FieldDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.InSetConstraintDTO;
import com.scottlogic.datahelix.generator.profile.validators.profile.FieldValidator;
import com.scottlogic.datahelix.generator.profile.validators.profile.constraints.capabilities.ValueTypeValidator;

import java.util.List;

public class InSetConstraintValidator extends AtomicConstraintValidator<InSetConstraintDTO>
{
    public InSetConstraintValidator(List<FieldDTO> fields)
    {
        super(fields);
    }

    @Override
    public final ValidationResult validate(InSetConstraintDTO dto)
    {
        ValidationResult result = ValidationResult.combine(valuesMustBeSpecified(dto), fieldMustBeValid(dto));
        if (!result.isSuccess) return result;

        return fieldTypeMustBeValid(dto);
    }

    private ValidationResult valuesMustBeSpecified(InSetConstraintDTO dto)
    {
        return dto.values != null && !dto.values.isEmpty()
            ? ValidationResult.success()
            : ValidationResult.failure("In set values must be specified" + getErrorInfo(dto));
    }

    private ValidationResult fieldTypeMustBeValid(InSetConstraintDTO dto)
    {
        FieldType fieldType = FieldValidator.getSpecificFieldType(getField(dto.field)).getFieldType();
        ValueTypeValidator valueTypeValidator = new ValueTypeValidator(fieldType, getErrorInfo(dto));
        return ValidationResult.combine(dto.values.stream()
                .map(InSetRecord::getElement)
                .map(valueTypeValidator::validate)
            );
    }
}
