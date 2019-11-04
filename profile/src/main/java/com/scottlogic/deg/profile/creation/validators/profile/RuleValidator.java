package com.scottlogic.deg.profile.creation.validators.profile;

import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.common.validators.Validator;
import com.scottlogic.deg.profile.creation.dtos.FieldDTO;
import com.scottlogic.deg.profile.creation.dtos.RuleDTO;

import java.util.List;

public class RuleValidator implements Validator<RuleDTO>
{
    private final List<FieldDTO> fields;

    public RuleValidator(List<FieldDTO> fields)
    {

        this.fields = fields;
    }

    @Override
    public ValidationResult validate(RuleDTO rule)
    {
        String ruleDescription = rule.description == null ? "Unnamed rule" : rule.description;
        ValidationResult constraintsMustBeSpecified = constraintsMustBeSpecified(rule, ruleDescription);
        if(!constraintsMustBeSpecified.isSuccess) return constraintsMustBeSpecified;
        return ValidationResult.combine(rule.constraints.stream().map(c -> ConstraintValidator.validateConstraint(c, ruleDescription, fields)));
    }

    private ValidationResult constraintsMustBeSpecified(RuleDTO rule, String ruleDescription)
    {
        return rule.constraints != null && !rule.constraints.isEmpty()
            ? ValidationResult.success()
            : ValidationResult.failure("Constraints must be specified | Rule: " + ruleDescription);
    }

}
