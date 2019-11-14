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

import com.scottlogic.datahelix.generator.common.validators.ValidationResult;
import com.scottlogic.datahelix.generator.profile.dtos.FieldDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.InSetConstraintDTO;

import java.util.List;

public class InSetConstraintValidator extends AtomicConstraintValidator<InSetConstraintDTO>
{
    public InSetConstraintValidator(String rule, List<FieldDTO> fields)
    {
        super(rule, fields);
    }

    @Override
    public final ValidationResult validate(InSetConstraintDTO dto)
    {
        ValidationResult result = ValidationResult.combine(valuesMustBeSpecified(dto),fieldMustBeValid(dto));
        if(!result.isSuccess) return result;

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
        return ValidationResult.combine(dto.values.stream().map(v -> fieldTypeMustMatchValueType(dto, v)));
    }
}
