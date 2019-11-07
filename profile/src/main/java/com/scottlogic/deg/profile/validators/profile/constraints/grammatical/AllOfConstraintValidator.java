package com.scottlogic.deg.profile.validators.profile.constraints.grammatical;

import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.profile.dtos.FieldDTO;
import com.scottlogic.deg.profile.dtos.constraints.grammatical.AllOfConstraintDTO;

import java.util.List;

public class AllOfConstraintValidator extends GrammaticalConstraintValidator<AllOfConstraintDTO>
{

    public AllOfConstraintValidator(String rule, List<FieldDTO> fields)
    {
        super(rule, fields);
    }

    @Override
    public ValidationResult validate(AllOfConstraintDTO allOfConstraint)
    {
        return subConstraintsMustBeValid(allOfConstraint.constraints, allOfConstraint);
    }
}
