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

package com.scottlogic.datahelix.generator.profile.validators.profile.constraints;

import com.scottlogic.datahelix.generator.common.validators.ValidationResult;
import com.scottlogic.datahelix.generator.profile.dtos.FieldDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.relations.InMapConstraintDTO;
import com.scottlogic.datahelix.generator.profile.validators.profile.ConstraintValidator;

import java.util.List;
import java.util.Optional;

public class InMapConstraintValidator extends ConstraintValidator<InMapConstraintDTO>
{
    public InMapConstraintValidator(List<FieldDTO> fields)
    {
        super(fields);
    }

    @Override
    public ValidationResult validate(InMapConstraintDTO inMapConstraint)
    {
        return ValidationResult.combine(fieldMustBeValid(inMapConstraint), valuesMustBeSpecified(inMapConstraint));
    }

    private ValidationResult fieldMustBeValid(InMapConstraintDTO dto)
    {
        if (dto.field == null || dto.field.isEmpty())
        {
            return ValidationResult.failure("Field must be specified" + getErrorInfo(dto));
        }
        Optional<FieldDTO> field = fields.stream().filter(f -> f.name.equals(dto.field)).findFirst();
        if (!field.isPresent())
        {
            return ValidationResult.failure(dto.field + " must be defined in fields" + getErrorInfo(dto));
        }
        return ValidationResult.success();
    }

    private ValidationResult valuesMustBeSpecified(InMapConstraintDTO inMapConstraint)
    {
        return inMapConstraint.values != null && !inMapConstraint.values.isEmpty()
            ? ValidationResult.success()
            : ValidationResult.failure("Values must be specified" + getErrorInfo(inMapConstraint));
    }
}
