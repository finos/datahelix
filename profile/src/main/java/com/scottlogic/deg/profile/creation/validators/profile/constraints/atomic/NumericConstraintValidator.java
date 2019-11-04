package com.scottlogic.deg.profile.creation.validators.profile.constraints.atomic;


import com.scottlogic.deg.common.profile.FieldType;
import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.profile.creation.dtos.FieldDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.atomic.numeric.NumericConstraintDTO;

import java.util.List;

public class NumericConstraintValidator extends AtomicConstraintValidator<NumericConstraintDTO>
{
    private final FieldType expectedFieldType;

    public NumericConstraintValidator(String rule, List<FieldDTO> fields, FieldType expectedFieldType)
    {
        super(rule, fields);
        this.expectedFieldType = expectedFieldType;
    }

    @Override
    public final ValidationResult validate(NumericConstraintDTO dto)
    {
        ValidationResult fieldMustBeValid = fieldMustBeValid(dto);
        if(!fieldMustBeValid.isSuccess) return fieldMustBeValid;

        ValidationResult numberMustBeSpecified = numberMustBeSpecified(dto);
        if(!numberMustBeSpecified.isSuccess) return numberMustBeSpecified;

        return valueMustBeValid(dto, expectedFieldType);
    }

    private ValidationResult numberMustBeSpecified(NumericConstraintDTO dto)
    {
        return dto.getNumber() != null
            ? ValidationResult.success()
            : ValidationResult.failure("Number must be specified" + getErrorInfo(dto));
    }
}
