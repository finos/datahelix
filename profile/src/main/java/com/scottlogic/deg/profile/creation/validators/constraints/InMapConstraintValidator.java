package com.scottlogic.deg.profile.creation.validators.constraints;

import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.profile.creation.dtos.FieldDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.InMapConstraintDTO;

import java.util.List;

public class InMapConstraintValidator extends ConstraintValidator<InMapConstraintDTO>
{
    public InMapConstraintValidator(List<FieldDTO> fields, String rule)
    {
        super(fields, rule);
    }

    @Override
    public ValidationResult validate(InMapConstraintDTO inMapConstraint)
    {
        ValidationResult baseValidationResult = super.validate(inMapConstraint);
        if (!baseValidationResult.isValid) return baseValidationResult;

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
            return ValidationResult.failure("Atomic constraint's referenced field must be specified");
        }
        if (fields.stream().noneMatch(f -> f.name.equals(inMapConstraint.field)))
        {
            return ValidationResult.failure("Atomic constraint's referenced field "+ field + " must be defined in fields | Rule: " + rule);
        }
        return ValidationResult.success();
    }

    private ValidationResult fileMustBeSpecified(InMapConstraintDTO inMapConstraint)
    {
        return inMapConstraint.file != null && !inMapConstraint.key.isEmpty()
            ? ValidationResult.success()
            : ValidationResult.failure("File must be specified | Rule: " + rule + " | Constraint: " + inMapConstraint.getName());
    }

    private ValidationResult keyMustBeSpecified(InMapConstraintDTO inMapConstraint)
    {
        return inMapConstraint.key != null && !inMapConstraint.key.isEmpty()
            ? ValidationResult.success()
            : ValidationResult.failure("Key must be specified | Rule: " + rule + " | Constraint: " + inMapConstraint.getName());
    }
}
