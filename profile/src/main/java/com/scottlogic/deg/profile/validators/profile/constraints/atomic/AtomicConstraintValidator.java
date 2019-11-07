package com.scottlogic.deg.profile.validators.profile.constraints.atomic;

import com.scottlogic.deg.common.profile.FieldType;
import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.profile.dtos.FieldDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.AtomicConstraintDTO;
import com.scottlogic.deg.profile.validators.profile.ConstraintValidator;

import java.util.List;
import java.util.Optional;

abstract class AtomicConstraintValidator<T extends AtomicConstraintDTO> extends ConstraintValidator<T>
{
    AtomicConstraintValidator(String rule, List<FieldDTO> fields)
    {
        super(rule, fields);
    }

    @Override
    protected String getErrorInfo(T atomicConstraint)
    {
        return " | Field: " + atomicConstraint.field + super.getErrorInfo(atomicConstraint);
    }


    ValidationResult valueMustBeValid(T dto, FieldType expectedFieldType)
    {
        FieldType fieldType = fields.stream().filter(f -> f.name.equals(dto.field)).findFirst().get().type.getFieldType();
        if (expectedFieldType != fieldType)
        {
            return ValidationResult.failure("Expected field type " + expectedFieldType + " doesn't match field type " + fieldType + getErrorInfo(dto));
        }
        return ValidationResult.success();
    }

    ValidationResult valueMustBeValid(T dto, Object value)
    {
        if(value == null)
        {
            return ValidationResult.failure("Values must be specified" + getErrorInfo(dto));
        }
        FieldType fieldType = fields.stream().filter(f -> f.name.equals(dto.field)).findFirst().get().type.getFieldType();
        if (!(value instanceof Number || value instanceof String && isNumber((String)value)) && fieldType == FieldType.NUMERIC)
        {
            return ValidationResult.failure("Value " + value + " must be number for field type " + fieldType + getErrorInfo(dto));
        }
        if (!(value instanceof String) && fieldType != FieldType.NUMERIC)
        {
            return ValidationResult.failure("Value " + value + " must be string for field type " + fieldType + getErrorInfo(dto));
        }
        return ValidationResult.success();
    }

    private static boolean isNumber(String s)
    {
        try
        {
            Double.parseDouble(s);
        }
        catch (NumberFormatException e)
        {
            return false;
        }
        catch (NullPointerException e)
        {
            return false;
        }
        return true;
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
