package com.scottlogic.deg.profile.creation.validators.constraints;

import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.profile.creation.dtos.FieldDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.NotConstraintDTO;

import java.util.List;

public class NotConstraintValidator extends ConstraintValidator<NotConstraintDTO>
{
    public NotConstraintValidator(String rule, List<FieldDTO> fields)
    {
        super(rule, fields);
    }

    @Override
    public ValidationResult validate(NotConstraintDTO notConstraint)
    {
        return ConstraintValidator.validateConstraint(notConstraint.constraint, rule, fields);
    }
}
