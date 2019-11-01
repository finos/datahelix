package com.scottlogic.deg.profile.creation.validators.constraints;

import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.common.validators.Validator;
import com.scottlogic.deg.profile.creation.dtos.FieldDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.ConstraintDTO;
import com.scottlogic.deg.profile.creation.validators.constraints.grammatical.AllOfConstraintValidator;
import com.scottlogic.deg.profile.creation.validators.constraints.grammatical.AnyOfConstraintValidator;
import com.scottlogic.deg.profile.creation.validators.constraints.grammatical.ConditionalConstraintValidator;

import java.util.List;

public abstract class ConstraintValidator<TConstraintDTO extends ConstraintDTO> implements Validator<TConstraintDTO>
{
    protected final String rule;
    protected final List<FieldDTO> fields;

    protected ConstraintValidator(String rule, List<FieldDTO> fields)
    {
        this.rule = rule;
        this.fields = fields;
    }

    public static ValidationResult validateConstraint(ConstraintDTO constraint, String rule, List<FieldDTO> fields)
    {
        ValidationResult constraintMustBeSpecified = constraintMustBeSpecified(constraint, rule);
        if (!constraintMustBeSpecified.isValid) return constraintMustBeSpecified;

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
                constraintValidator = new NotConstraintValidator(rule, fields);
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

    private static ValidationResult constraintMustBeSpecified(ConstraintDTO constraint, String rule)
    {
        if(constraint == null)
        {
            return  ValidationResult.failure("Constraint must not be null | Rule: " + rule);
        }
        if(constraint.getType() == null)
        {
            ValidationResult.failure("Constraint type must not be null | Rule: " + rule);
        }
        return ValidationResult.success();
    }
}
