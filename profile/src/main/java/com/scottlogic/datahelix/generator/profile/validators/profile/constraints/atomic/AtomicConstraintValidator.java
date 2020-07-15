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
import com.scottlogic.datahelix.generator.common.validators.ValidationResult;
import com.scottlogic.datahelix.generator.common.whitelist.WeightedElement;
import com.scottlogic.datahelix.generator.profile.dtos.FieldDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.AtomicConstraintDTO;
import com.scottlogic.datahelix.generator.profile.validators.profile.ConstraintValidator;

import java.util.List;
import java.util.Optional;

abstract class AtomicConstraintValidator<T extends AtomicConstraintDTO> extends ConstraintValidator<T>
{
    AtomicConstraintValidator(List<FieldDTO> fields)
    {
        super(fields);
    }

    @Override
    protected String getErrorInfo(T atomicConstraint)
    {
        return " | Field: " + atomicConstraint.field + super.getErrorInfo(atomicConstraint);
    }

    ValidationResult fieldTypeMustMatchValueType(T dto, FieldType expectedFieldType)
    {
        FieldType fieldType = getFieldType(dto.field);
        if (expectedFieldType != fieldType)
        {
            return ValidationResult.failure("Expected field type " + expectedFieldType + " doesn't match field type " + fieldType + getErrorInfo(dto));
        }
        return ValidationResult.success();
    }

    ValidationResult fieldTypeMustMatchValueType(T dto, Object value)
    {
        if(value == null)
        {
            return ValidationResult.failure("Values must be specified" + getErrorInfo(dto));
        }
        FieldType fieldType = getFieldType(dto.field);
        if(value instanceof Boolean && fieldType != FieldType.BOOLEAN)
        {
            return ValidationResult.failure("Value " + value + " must be a boolean" + getErrorInfo(dto));
        }
        if (!(value instanceof Number || value instanceof String && isNumber((String)value) ||
            value instanceof WeightedElement && !(((WeightedElement) value).element() instanceof Number)) &&
            fieldType == FieldType.NUMERIC)
        {
            return ValidationResult.failure("Value " + value + " must be a number" + getErrorInfo(dto));
        }
        if (value instanceof Number && fieldType != FieldType.NUMERIC)
        {
            return ValidationResult.failure("Value " + value + " must be a string" + getErrorInfo(dto));
        }
        return ValidationResult.success();
    }

    private static boolean isNumber(String s)
    {
        try
        {
            Double.parseDouble(s);
            return true;
        }
        catch (NumberFormatException | NullPointerException e)
        {
            return false;
        }
    }


    ValidationResult fieldMustBeValid(T dto)
    {
        String fieldName = dto.field;
        if (fieldName == null || fieldName.isEmpty())
        {
            return ValidationResult.failure("Field must be specified" + getErrorInfo(dto));
        }
        Optional<FieldDTO> field = fields.stream().filter(f -> f.name.equals(fieldName)).findFirst();
        if (!field.isPresent())
        {
            return ValidationResult.failure(fieldName + " must be defined in fields" + getErrorInfo(dto));
        }
        return ValidationResult.success();
    }
}
