package com.scottlogic.deg.profile.validators.profile.constraints.atomic;


import com.scottlogic.deg.common.profile.FieldType;
import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.profile.dtos.FieldDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.textual.RegexConstraintDTO;

import java.util.List;
import java.util.regex.Pattern;

public class RegexConstraintValidator extends AtomicConstraintValidator<RegexConstraintDTO>
{
    public RegexConstraintValidator(String rule, List<FieldDTO> fields)
    {
        super(rule, fields);
    }

    @Override
    public final ValidationResult validate(RegexConstraintDTO dto)
    {
        ValidationResult fieldMustBeValid = fieldMustBeValid(dto);
        if(!fieldMustBeValid.isSuccess) return fieldMustBeValid;

        ValidationResult regexMustBeValid = regexMustBeValid(dto);
        if(!regexMustBeValid.isSuccess) return regexMustBeValid;

        return valueMustBeValid(dto, FieldType.STRING);
    }

    private ValidationResult regexMustBeValid(RegexConstraintDTO dto)
    {
        ValidationResult regexMustBeSpecified = regexMustBeSpecified(dto);
        if(!regexMustBeSpecified.isSuccess) return regexMustBeSpecified;
        try
        {
             Pattern.compile(dto.getRegex());
             return ValidationResult.success();
        }
        catch (Exception e)
        {
            return ValidationResult.failure("Regex is invalid | " + e.getMessage());
        }
    }

    private ValidationResult regexMustBeSpecified(RegexConstraintDTO dto)
    {
        String regex = dto.getRegex();
        return regex != null && !regex.isEmpty()
            ? ValidationResult.success()
            : ValidationResult.failure("Text must be specified" + getErrorInfo(dto));
    }
}
