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
import com.scottlogic.datahelix.generator.profile.validators.profile.ConstraintValidator;
import com.scottlogic.datahelix.generator.profile.validators.profile.FieldValidator;

import java.util.List;
import java.util.Optional;

public class RelationalConstraintValidator<T extends RelationalConstraintDTO> extends ConstraintValidator<T>
{
    public RelationalConstraintValidator(List<FieldDTO> fields)
    {
        super(fields);
    }

    @Override
    public ValidationResult validate(T dto)
    {
        String fieldName = dto.field;
        String otherFieldName = dto.getOtherField();
        if (fieldName == null || fieldName.isEmpty())
        {
            return ValidationResult.failure("Field must be specified" + getErrorInfo(dto));
        }
        if (otherFieldName == null || otherFieldName.isEmpty())
        {
            return ValidationResult.failure("Related field must be specified" + getErrorInfo(dto));
        }
        Optional<FieldDTO> field = fields.stream().filter(f -> f.name.equals(fieldName)).findFirst();
        if (!field.isPresent())
        {
            return ValidationResult.failure(String.format("%s must be defined in fields%s", ValidationResult.quote(fieldName), getErrorInfo(dto)));
        }
        Optional<FieldDTO> otherField = fields.stream().filter(f -> f.name.equals(otherFieldName)).findFirst();
        if (!otherField.isPresent())
        {
            return ValidationResult.failure(String.format("%s must be defined in fields%s", ValidationResult.quote(otherFieldName), getErrorInfo(dto)));
        }
        FieldType fieldType = FieldValidator.getSpecificFieldType(field.get()).getFieldType();
        FieldType otherFieldType = FieldValidator.getSpecificFieldType(otherField.get()).getFieldType();
        if (fieldType != otherFieldType)
        {
            return ValidationResult.failure(String.format("Field type %s doesn't match related field type %s%s", ValidationResult.quote(fieldName), ValidationResult.quote(otherFieldName), getErrorInfo(dto)));
        }
        if (dto.offsetUnit != null && !dto.offsetUnit.isEmpty())
        {
            return validateGranularity(dto, dto.field, dto.offsetUnit);
        }
        return ValidationResult.success();
    }
}
