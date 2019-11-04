package com.scottlogic.deg.profile.creation.validators.profile.constraints.atomic;

import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.profile.creation.dtos.FieldDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.atomic.InSetConstraintDTO;

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
        return ValidationResult.combine(dto.values.stream().map(v -> fieldTypeMustBeValid(dto, v)));
    }
}
