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
        return null;
    }
}
