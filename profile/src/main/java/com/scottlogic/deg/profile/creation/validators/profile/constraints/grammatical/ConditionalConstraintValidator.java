package com.scottlogic.deg.profile.creation.validators.profile.constraints.grammatical;

import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.profile.creation.dtos.FieldDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.grammatical.ConditionalConstraintDTO;

import java.util.List;

public class ConditionalConstraintValidator  extends GrammaticalConstraintValidator<ConditionalConstraintDTO>
{

    public ConditionalConstraintValidator(String rule, List<FieldDTO> fields)
    {
        super(rule, fields);
    }

    @Override
    public ValidationResult validate(ConditionalConstraintDTO conditionalConstraint)
    {
        ValidationResult validateIfConstraint = validateConstraint(conditionalConstraint.ifConstraint, rule, fields);
        ValidationResult validateThenConstraint = validateConstraint(conditionalConstraint.thenConstraint, rule, fields);
        ValidationResult validateElseConstraint = conditionalConstraint.elseConstraint == null
            ? ValidationResult.success()
            :validateConstraint(conditionalConstraint.ifConstraint, rule, fields);

        return ValidationResult.combine(validateIfConstraint, validateThenConstraint, validateElseConstraint);
    }
}
