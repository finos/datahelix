package com.scottlogic.deg.profile.validators.profile.constraints.atomic;

import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.profile.dtos.FieldDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.EqualToConstraintDTO;

import java.util.List;

public class EqualToConstraintValidator extends AtomicConstraintValidator<EqualToConstraintDTO>
{
    public EqualToConstraintValidator(String rule, List<FieldDTO> fields)
    {
        super(rule, fields);
    }

    @Override
    public final ValidationResult validate(EqualToConstraintDTO dto)
    {
        ValidationResult fieldMustBeValid = fieldMustBeValid(dto);
        if(!fieldMustBeValid.isSuccess) return fieldMustBeValid;
        return valueMustBeValid(dto, dto.value);
    }

}
