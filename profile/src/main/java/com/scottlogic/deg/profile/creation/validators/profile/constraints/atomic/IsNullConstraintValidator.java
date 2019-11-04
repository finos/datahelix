package com.scottlogic.deg.profile.creation.validators.profile.constraints.atomic;

import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.profile.creation.dtos.FieldDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.atomic.IsNullConstraintDTO;

import java.util.List;

public class IsNullConstraintValidator extends AtomicConstraintValidator<IsNullConstraintDTO>
{
    public IsNullConstraintValidator(String rule, List<FieldDTO> fields)
    {
        super(rule, fields);
    }

    @Override
    public final ValidationResult validate(IsNullConstraintDTO dto)
    {
        return fieldMustBeValid(dto);
    }
}
