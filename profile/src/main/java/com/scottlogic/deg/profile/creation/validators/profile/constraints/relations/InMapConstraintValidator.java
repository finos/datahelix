package com.scottlogic.deg.profile.creation.validators.profile.constraints.relations;

import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.profile.creation.dtos.FieldDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.relations.InMapConstraintDTO;

import java.util.List;

public class InMapConstraintValidator extends RelationalConstraintValidator<InMapConstraintDTO>
{
    public InMapConstraintValidator(String rule, List<FieldDTO> fields)
    {
        super(rule, fields);
    }

    @Override
    public ValidationResult validate(InMapConstraintDTO inMapConstraint)
    {
        return ValidationResult.combine(super.validate(inMapConstraint), valuesMustBeSpecified(inMapConstraint));
    }

    private ValidationResult valuesMustBeSpecified(InMapConstraintDTO inMapConstraint)
    {
        return inMapConstraint.values != null && !inMapConstraint.values.isEmpty()
            ? ValidationResult.success()
            : ValidationResult.failure("Values must be specified" + getErrorInfo(inMapConstraint));
    }
}
