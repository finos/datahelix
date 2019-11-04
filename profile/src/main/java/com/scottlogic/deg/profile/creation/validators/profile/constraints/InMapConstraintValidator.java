package com.scottlogic.deg.profile.creation.validators.profile.constraints;

import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.profile.creation.dtos.FieldDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.relations.InMapConstraintDTO;
import com.scottlogic.deg.profile.creation.validators.profile.ConstraintValidator;

import java.util.List;
import java.util.Optional;

public class InMapConstraintValidator extends ConstraintValidator<InMapConstraintDTO>
{
    public InMapConstraintValidator(String rule, List<FieldDTO> fields)
    {
        super(rule, fields);
    }

    @Override
    public ValidationResult validate(InMapConstraintDTO inMapConstraint)
    {
        return ValidationResult.combine(fieldMustBeValid(inMapConstraint), valuesMustBeSpecified(inMapConstraint));
    }

    private ValidationResult fieldMustBeValid(InMapConstraintDTO dto)
    {
        if (dto.field == null || dto.field.isEmpty())
        {
            return ValidationResult.failure("Field must be specified" + getErrorInfo(dto));
        }
        Optional<FieldDTO> field = fields.stream().filter(f -> f.name.equals(dto.field)).findFirst();
        if (!field.isPresent())
        {
            return ValidationResult.failure(dto.field + " must be defined in fields" + getErrorInfo(dto));
        }
        return ValidationResult.success();
    }

    private ValidationResult valuesMustBeSpecified(InMapConstraintDTO inMapConstraint)
    {
        return inMapConstraint.values != null && !inMapConstraint.values.isEmpty()
            ? ValidationResult.success()
            : ValidationResult.failure("Values must be specified" + getErrorInfo(inMapConstraint));
    }
}
