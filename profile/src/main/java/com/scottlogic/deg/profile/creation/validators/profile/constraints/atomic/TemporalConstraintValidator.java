package com.scottlogic.deg.profile.creation.validators.profile.constraints.atomic;


import com.scottlogic.deg.common.profile.FieldType;
import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.profile.creation.dtos.FieldDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.atomic.temporal.TemporalConstraintDTO;

import java.util.List;

public class TemporalConstraintValidator extends AtomicConstraintValidator<TemporalConstraintDTO>
{
    public TemporalConstraintValidator(String rule, List<FieldDTO> fields)
    {
        super(rule, fields);
    }

    @Override
    public final ValidationResult validate(TemporalConstraintDTO dto)
    {
        ValidationResult fieldMustBeValid = fieldMustBeValid(dto);
        if(!fieldMustBeValid.isSuccess) return fieldMustBeValid;

        ValidationResult dateMustBeSpecified = dateMustBeSpecified(dto);
        if(!dateMustBeSpecified.isSuccess) return dateMustBeSpecified;

        return valueMustBeValid(dto, FieldType.DATETIME);
    }


    private ValidationResult dateMustBeSpecified(TemporalConstraintDTO dto)
    {
        String date = dto.getDate();
        return date != null && !date.isEmpty()
            ? ValidationResult.success()
            : ValidationResult.failure("Date must be specified" + getErrorInfo(dto));
    }
}
