package com.scottlogic.deg.profile.validators.profile.constraints.grammatical;

import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.profile.dtos.FieldDTO;
import com.scottlogic.deg.profile.dtos.constraints.ConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.grammatical.GrammaticalConstraintDTO;
import com.scottlogic.deg.profile.validators.profile.ConstraintValidator;

import java.util.List;

abstract class GrammaticalConstraintValidator<T extends GrammaticalConstraintDTO> extends ConstraintValidator<T>
{
    GrammaticalConstraintValidator(String rule, List<FieldDTO> fields)
    {
        super(rule, fields);
    }

    ValidationResult subConstraintsMustBeValid(List<ConstraintDTO> subConstraints, T grammaticalConstraint)
    {
        ValidationResult subConstraintsMustBeSpecified = subConstraintsMustBeSpecified(subConstraints, grammaticalConstraint);
        if(!subConstraintsMustBeSpecified.isSuccess) return subConstraintsMustBeSpecified;

        return ValidationResult.combine(subConstraints.stream().map(c -> validateConstraint(c, rule, fields)));
    }

    private ValidationResult subConstraintsMustBeSpecified(List<ConstraintDTO> constraints, T constraint)
    {
        return constraints != null && !constraints.isEmpty()
            ? ValidationResult.success()
            : ValidationResult.failure("Sub constraints must be specified" + getErrorInfo(constraint));
    }
}
