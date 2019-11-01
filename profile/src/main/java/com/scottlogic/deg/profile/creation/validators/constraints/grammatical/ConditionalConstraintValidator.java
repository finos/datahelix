package com.scottlogic.deg.profile.creation.validators.constraints.grammatical;

import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.profile.creation.dtos.FieldDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.grammatical.ConditionalConstraintDTO;
import com.scottlogic.deg.profile.creation.validators.constraints.ConstraintValidator;

import java.util.List;

public class ConditionalConstraintValidator  extends ConstraintValidator<ConditionalConstraintDTO>
{

    public ConditionalConstraintValidator(String rule, List<FieldDTO> fields)
    {
        super(rule, fields);
    }

    @Override
    public ValidationResult validate(ConditionalConstraintDTO conditionalConstraint)
    {
        return null;
    }
}
