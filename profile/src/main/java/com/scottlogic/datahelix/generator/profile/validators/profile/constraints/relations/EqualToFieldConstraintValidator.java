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
import com.scottlogic.datahelix.generator.profile.dtos.constraints.relations.EqualToFieldConstraintDTO;
import com.scottlogic.datahelix.generator.profile.validators.profile.FieldValidator;

import java.util.List;

public class EqualToFieldConstraintValidator extends RelationalConstraintValidator<EqualToFieldConstraintDTO>
{

    public EqualToFieldConstraintValidator(List<FieldDTO> fields)
    {
        super(fields);
    }

    @Override
    public ValidationResult validate(EqualToFieldConstraintDTO dto)
    {
        ValidationResult fieldsAndOffsetValid = ValidationResult.combine(
            fieldMustBeValid(dto, dto.field, FIELD_DESCRIPTION),
            fieldMustBeValid(dto, dto.getOtherField(), RELATED_FIELD_DESCRIPTION),
            offsetMustBeValid(dto)
        );
        return fieldsAndOffsetValid.isSuccess ? fieldAndOtherFieldMustHaveType(dto) : fieldsAndOffsetValid;
    }

    private ValidationResult fieldAndOtherFieldMustHaveType(EqualToFieldConstraintDTO dto)
    {
        FieldType fieldType = FieldValidator.getSpecificFieldType(getField(dto.field)).getFieldType();
        FieldType otherFieldType = FieldValidator.getSpecificFieldType(getField(dto.getOtherField())).getFieldType();

        return fieldType == otherFieldType
            ? ValidationResult.success()
            : ValidationResult.failure(String.format("Field type %s doesn't match related field type %s%s", ValidationResult.quote(dto.field), ValidationResult.quote(dto.getOtherField()), getErrorInfo(dto)));
    }
}
