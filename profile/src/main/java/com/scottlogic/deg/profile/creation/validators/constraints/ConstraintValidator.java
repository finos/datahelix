package com.scottlogic.deg.profile.creation.validators.constraints;

import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.common.validators.Validator;
import com.scottlogic.deg.profile.creation.dtos.FieldDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.ConstraintDTO;

import java.util.List;

public abstract class ConstraintValidator<TConstraintDTO extends ConstraintDTO> implements Validator<TConstraintDTO>
{
    protected final List<FieldDTO> fields;
    protected final String rule;

    protected ConstraintValidator(List<FieldDTO> fields, String rule)
    {
        this.fields = fields;
        this.rule = rule;
    }

    @Override
    public ValidationResult validate(TConstraintDTO constraint)
    {
        return constraintMustBeSpecified(constraint);
    }

    private ValidationResult constraintMustBeSpecified(ConstraintDTO constraint)
    {
        return constraint != null
            ? ValidationResult.success()
            : ValidationResult.failure("Constraint must not be null | Rule: " + rule);
    }
}
