package com.scottlogic.deg.profile.creation.validators.profile;

import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.common.validators.Validator;
import com.scottlogic.deg.profile.creation.dtos.FieldDTO;
import com.scottlogic.deg.profile.creation.dtos.ProfileDTO;
import com.scottlogic.deg.profile.creation.dtos.RuleDTO;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProfileValidator implements Validator<ProfileDTO>
{
    @Override
    public ValidationResult validate(ProfileDTO profile)
    {
        List<FieldDTO> fields = profile.fields;
        List<RuleDTO> rules = profile.rules;

        ValidationResult fieldsMustBeValid = fieldsMustBeValid(fields);
        if (!fieldsMustBeValid.isSuccess) return fieldsMustBeValid;

        ValidationResult rulesMustBeValid = rulesMustBeValid(rules, fields);
        if (!rulesMustBeValid.isSuccess) return rulesMustBeValid;

        return ValidationResult.success();
    }

    private ValidationResult fieldsMustBeValid(List<FieldDTO> fields)
    {
        ValidationResult fieldsMustBeSpecified = fieldsMustBeSpecified(fields);
        if (!fieldsMustBeSpecified.isSuccess) return fieldsMustBeSpecified;

        ValidationResult fieldsMustBeUnique  = fieldsMustBeUnique(fields);
        if(!fieldsMustBeUnique.isSuccess) return fieldsMustBeUnique;

        FieldValidator fieldValidator = new FieldValidator();
        return ValidationResult.combine(fields.stream().map(fieldValidator::validate));
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

    private ValidationResult rulesMustBeValid(List<RuleDTO> rules, List<FieldDTO> fields)
    {
        ValidationResult rulesMustBeSpecified = rulesMustBeSpecified(rules);
        if (!rulesMustBeSpecified.isSuccess) return rulesMustBeSpecified;

        RuleValidator ruleValidator = new RuleValidator(fields);
        return ValidationResult.combine(rules.stream().map(ruleValidator::validate));
    }

    private ValidationResult rulesMustBeSpecified(List<RuleDTO> rules)
    {
        return rules != null
            ? ValidationResult.success()
            : ValidationResult.failure("Rules must be specified");
    }
}
