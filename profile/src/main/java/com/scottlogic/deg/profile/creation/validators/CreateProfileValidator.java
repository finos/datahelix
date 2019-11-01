package com.scottlogic.deg.profile.creation.validators;

import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.common.validators.Validator;
import com.scottlogic.deg.profile.creation.commands.CreateProfile;
import com.scottlogic.deg.profile.creation.dtos.FieldDTO;
import com.scottlogic.deg.profile.creation.dtos.RuleDTO;
import com.scottlogic.deg.profile.creation.services.ConstraintValidationService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CreateProfileValidator implements Validator<CreateProfile>
{
    @Override
    public ValidationResult validate(CreateProfile createProfile)
    {
        List<FieldDTO> fields = createProfile.profileDTO.fields;
        List<RuleDTO> rules = createProfile.profileDTO.rules;

        ValidationResult fieldsMustBeValid = fieldsMustBeValid(fields);
        if (!fieldsMustBeValid.isValid) return fieldsMustBeValid;

        ValidationResult rulesMustBeValid = rulesMustBeValid(rules, fields);
        if (!rulesMustBeValid.isValid) return rulesMustBeValid;

        return ValidationResult.success();
    }

    private ValidationResult fieldsMustBeSpecified(List<FieldDTO> fields)
    {
        return fields != null && !fields.isEmpty()
            ? ValidationResult.success()
            : ValidationResult.failure("Fields must be specified");
    }

    private ValidationResult fieldsMustBeUnique(List<FieldDTO> fields)
    {
        final Set<String> fieldNames = new HashSet<>();
        final Set<String> duplicateFieldNames = new HashSet<>();

        fields.forEach(field ->
        {
            if(!fieldNames.add(field.name))
            {
                duplicateFieldNames.add(field.name);
            }
        });

        return duplicateFieldNames.isEmpty()
            ? ValidationResult.success()
            : ValidationResult.failure("Field names must be unique | Duplicates: "
            + String.join(", ", duplicateFieldNames));
    }

    private ValidationResult fieldsMustBeValid(List<FieldDTO> fields)
    {
        ValidationResult fieldsMustBeSpecified = fieldsMustBeSpecified(fields);
        if (!fieldsMustBeSpecified.isValid) return fieldsMustBeSpecified;

        FieldValidator fieldValidator = new FieldValidator();
        ValidationResult fieldsMustBeValid = ValidationResult.combine(fields.stream().map(fieldValidator::validate));
        return ValidationResult.combine(fieldsMustBeValid, fieldsMustBeUnique(fields));
    }

    private ValidationResult rulesMustBeSpecified(List<RuleDTO> rules)
    {
        return rules == null
            ? ValidationResult.success()
            : ValidationResult.failure("Rules must be specified");
    }

    private ValidationResult rulesMustBeValid(List<RuleDTO> rules, List<FieldDTO> fields)
    {
        ValidationResult rulesMustBeSpecified = rulesMustBeSpecified(rules);
        if (!rulesMustBeSpecified.isValid) return rulesMustBeSpecified;
        return ValidationResult.combine(rules.stream().map(rule -> constraintsMustBeValid(rule, fields)));
    }

    private ValidationResult constraintsMustBeSpecified(RuleDTO rule)
    {
        return rule.constraints != null && !rule.constraints.isEmpty()
            ? ValidationResult.success()
            : ValidationResult.failure("Constraints must be specified | Rule: "
            + (rule.description == null ? "Unnamed rule" : rule.description));
    }

    private ValidationResult constraintsMustBeValid(RuleDTO rule, List<FieldDTO> fields)
    {
        ValidationResult constraintsMustBeSpecified = constraintsMustBeSpecified(rule);
        if (!constraintsMustBeSpecified.isValid) return constraintsMustBeSpecified;

        String ruleDescription = rule.description == null ? "Unnamed rule" : rule.description;
        ConstraintValidationService constraintValidationService = new ConstraintValidationService(ruleDescription, fields);
        return ValidationResult.combine(rule.constraints.stream().map(constraintValidationService::validate));
    }
}
