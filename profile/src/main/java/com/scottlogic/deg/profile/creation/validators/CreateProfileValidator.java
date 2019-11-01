package com.scottlogic.deg.profile.creation.validators;

import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.common.validators.Validator;
import com.scottlogic.deg.profile.creation.commands.CreateProfile;
import com.scottlogic.deg.profile.creation.dtos.FieldDTO;
import com.scottlogic.deg.profile.creation.dtos.RuleDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.ConstraintDTO;
import com.scottlogic.deg.profile.creation.validators.constraints.ConstraintValidator;
import com.scottlogic.deg.profile.creation.validators.constraints.InMapConstraintValidator;
import com.scottlogic.deg.profile.creation.validators.constraints.NotConstraintValidator;
import com.scottlogic.deg.profile.creation.validators.constraints.grammatical.AllOfConstraintValidator;
import com.scottlogic.deg.profile.creation.validators.constraints.grammatical.AnyOfConstraintValidator;
import com.scottlogic.deg.profile.creation.validators.constraints.grammatical.ConditionalConstraintValidator;

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
        return ValidationResult.combine(rule.constraints.stream()
            .map(constraint -> constraintMustBeValid(constraint, ruleDescription, fields)));
    }

    private ValidationResult constraintMustBeSpecified(ConstraintDTO constraint, String rule)
    {
        if (constraint == null)
        {
            return ValidationResult.failure("Constraint must not be null | Rule: " + rule);
        }
        if(constraint.getType() == null)
        {
            return ValidationResult.failure("Constraint type must not be null | Rule: " + rule);
        }
        return ValidationResult.success();
    }

    private ValidationResult constraintMustBeValid(ConstraintDTO constraint, String rule, List<FieldDTO> fields)
    {
        ValidationResult constraintMustBeSpecified = constraintMustBeSpecified(constraint, rule);
        if(!constraintMustBeSpecified.isValid) return constraintMustBeSpecified;
        ConstraintValidator constraintValidator = null;
        switch (constraint.getType())
        {
            case EQUAL_TO:
                break;
            case EQUAL_TO_FIELD:
                break;
            case IN_SET:
                break;
            case IN_MAP:
                constraintValidator = new InMapConstraintValidator(fields, rule);
                break;
            case IS_NULL:
                break;
            case GRANULAR_TO:
                break;
            case MATCHES_REGEX:
                break;
            case CONTAINS_REGEX:
                break;
            case OF_LENGTH:
                break;
            case LONGER_THAN:
                break;
            case SHORTER_THAN:
                break;
            case GREATER_THAN:
                break;
            case GREATER_THAN_FIELD:
                break;
            case GREATER_THAN_OR_EQUAL_TO:
                break;
            case GREATER_THAN_OR_EQUAL_TO_FIELD:
                break;
            case LESS_THAN:
                break;
            case LESS_THAN_FIELD:
                break;
            case LESS_THAN_OR_EQUAL_TO:
                break;
            case LESS_THAN_OR_EQUAL_TO_FIELD:
                break;
            case AFTER:
                break;
            case AFTER_FIELD:
                break;
            case AFTER_OR_AT:
                break;
            case AFTER_OR_AT_FIELD:
                break;
            case BEFORE:
                break;
            case BEFORE_FIELD:
                break;
            case BEFORE_OR_AT:
                break;
            case BEFORE_OR_AT_FIELD:
                break;
            case NOT:
                constraintValidator = new NotConstraintValidator(fields, rule);
                break;
            case ANY_OF:
                constraintValidator = new AnyOfConstraintValidator(fields, rule);
                break;
            case ALL_OF:
                constraintValidator = new AllOfConstraintValidator(fields, rule);
                break;
            case IF:
                constraintValidator = new ConditionalConstraintValidator(fields, rule);
                break;
        }
        return constraintValidator.validate(constraint);
    }
}
