package com.scottlogic.deg.profile.creation.validators.profile.constraints.grammatical;

import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.profile.creation.dtos.FieldDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.grammatical.AnyOfConstraintDTO;

import java.util.List;

public class AnyOfConstraintValidator  extends GrammaticalConstraintValidator<AnyOfConstraintDTO>
{
    public AnyOfConstraintValidator(String rule, List<FieldDTO> fields)
    {
        super(rule, fields);
    }

    @Override
    public ValidationResult validate(AnyOfConstraintDTO anyOfConstraint)
    {
        return subConstraintsMustBeValid(anyOfConstraint.constraints, anyOfConstraint);
    }
}
