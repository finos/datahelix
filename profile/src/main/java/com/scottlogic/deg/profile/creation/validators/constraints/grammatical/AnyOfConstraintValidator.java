package com.scottlogic.deg.profile.creation.validators.constraints.grammatical;

import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.profile.creation.dtos.FieldDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.grammatical.AnyOfConstraintDTO;
import com.scottlogic.deg.profile.creation.validators.constraints.ConstraintValidator;

import java.util.List;

public class AnyOfConstraintValidator  extends ConstraintValidator<AnyOfConstraintDTO>
{

    public AnyOfConstraintValidator(String rule, List<FieldDTO> fields)
    {
        super(rule, fields);
    }

    @Override
    public ValidationResult validate(AnyOfConstraintDTO anyOfConstraint)
    {
        return null;
    }
}
