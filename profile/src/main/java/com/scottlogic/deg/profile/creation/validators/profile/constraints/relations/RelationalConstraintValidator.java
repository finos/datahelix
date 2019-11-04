package com.scottlogic.deg.profile.creation.validators.profile.constraints.relations;

import com.scottlogic.deg.common.profile.FieldType;
import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.profile.creation.dtos.FieldDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.relations.RelationalConstraintDTO;
import com.scottlogic.deg.profile.creation.validators.profile.ConstraintValidator;

import java.util.List;
import java.util.Optional;

public class RelationalConstraintValidator<T extends RelationalConstraintDTO> extends ConstraintValidator<T>
{
    public RelationalConstraintValidator(String rule, List<FieldDTO> fields)
    {
        super(rule, fields);
    }

    @Override
    public ValidationResult validate(T dto)
    {
        return fieldsMustBeValid(dto);
    }

    private ValidationResult fieldsMustBeValid(T dto)
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
            return ValidationResult.failure(fieldName + " must be defined in fields" + getErrorInfo(dto));
        }
        Optional<FieldDTO> otherField = fields.stream().filter(f -> f.name.equals(otherFieldName)).findFirst();
        if (!otherField.isPresent())
        {
            return ValidationResult.failure(otherFieldName + " must be defined in fields" + getErrorInfo(dto));
        }
        FieldType fieldType = field.get().type.getFieldType();
        FieldType otherFieldType = otherField.get().type.getFieldType();
        if (fieldType != otherFieldType)
        {
            return ValidationResult.failure("Field type " + fieldName + " doesn't match related field type " + otherFieldName + getErrorInfo(dto));
        }
        return ValidationResult.success();
    }
}
