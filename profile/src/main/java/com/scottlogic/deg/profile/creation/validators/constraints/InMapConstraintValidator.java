package com.scottlogic.deg.profile.creation.validators.constraints;

import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.profile.creation.dtos.FieldDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.InMapConstraintDTO;

import java.util.List;

public class InMapConstraintValidator extends ConstraintValidator<InMapConstraintDTO>
{
    public InMapConstraintValidator(String rule, List<FieldDTO> fields)
    {
        super(rule, fields);
    }

    @Override
    public ValidationResult validate(InMapConstraintDTO inMapConstraint)
    {
        return ValidationResult.combine
            (
                fileMustBeSpecified(inMapConstraint),
                keyMustBeSpecified(inMapConstraint),
                fieldMustBeValid(inMapConstraint)
            );
    }

    private ValidationResult fieldMustBeValid(InMapConstraintDTO inMapConstraint)
    {
        String field = inMapConstraint.field;
        if (field == null || field.isEmpty())
        {
            return ValidationResult.failure("Field must be specified" + getErrorInfo(inMapConstraint));
        }
        if (fields.stream().noneMatch(f -> f.name.equals(inMapConstraint.field)))
        {
            return ValidationResult.failure("Field "+ field + " must be defined in fields" + getErrorInfo(inMapConstraint));
        }
        return ValidationResult.success();
    }

    private ValidationResult fileMustBeSpecified(InMapConstraintDTO inMapConstraint)
    {
        return inMapConstraint.file != null && !inMapConstraint.key.isEmpty()
            ? ValidationResult.success()
            : ValidationResult.failure("File must be specified" + getErrorInfo(inMapConstraint));
    }

    private ValidationResult keyMustBeSpecified(InMapConstraintDTO inMapConstraint)
    {
        return inMapConstraint.key != null && !inMapConstraint.key.isEmpty()
            ? ValidationResult.success()
            : ValidationResult.failure("Key must be specified" + getErrorInfo(inMapConstraint));
    }
}
