package com.scottlogic.deg.profile.creation.validators.constraints.relations;

import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.common.validators.Validator;
import com.scottlogic.deg.profile.creation.dtos.constraints.relations.RelationalConstraintDTO;

public abstract class RelationalConstraintValidator implements Validator<RelationalConstraintDTO>
{
    @Override
    public ValidationResult validate(RelationalConstraintDTO relationalConstraint)
    {
        return null;
    }
}
