package com.scottlogic.deg.profile.creation.validators.profile;

import com.scottlogic.deg.common.profile.FieldType;
import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.common.validators.Validator;
import com.scottlogic.deg.profile.creation.dtos.FieldDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.ConstraintDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.InvalidConstraintDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.NotConstraintDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.atomic.InSetConstraintDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.atomic.IsNullConstraintDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.grammatical.AllOfConstraintDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.grammatical.AnyOfConstraintDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.grammatical.ConditionalConstraintDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.relations.InMapConstraintDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.atomic.EqualToConstraintDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.atomic.GranularToConstraintDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.atomic.numeric.NumericConstraintDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.atomic.temporal.*;
import com.scottlogic.deg.profile.creation.dtos.constraints.atomic.textual.RegexConstraintDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.relations.RelationalConstraintDTO;
import com.scottlogic.deg.profile.creation.validators.profile.constraints.InMapConstraintValidator;
import com.scottlogic.deg.profile.creation.validators.profile.constraints.NotConstraintValidator;
import com.scottlogic.deg.profile.creation.validators.profile.constraints.atomic.*;
import com.scottlogic.deg.profile.creation.validators.profile.constraints.grammatical.AllOfConstraintValidator;
import com.scottlogic.deg.profile.creation.validators.profile.constraints.grammatical.AnyOfConstraintValidator;
import com.scottlogic.deg.profile.creation.validators.profile.constraints.grammatical.ConditionalConstraintValidator;
import com.scottlogic.deg.profile.creation.validators.profile.constraints.RelationalConstraintValidator;

import java.util.List;

public abstract class ConstraintValidator<T extends ConstraintDTO> implements Validator<T>
{
    protected final String rule;
    protected final List<FieldDTO> fields;

    protected ConstraintValidator(String rule, List<FieldDTO> fields)
    {
        this.rule = rule;
        this.fields = fields;
    }

    protected String getErrorInfo(T constraint)
    {
        return " | Constraint: " + constraint.getName() + " | Rule: " + rule;
    }

    protected static ValidationResult validateConstraint(ConstraintDTO dto, String rule, List<FieldDTO> fields)
    {
        ValidationResult constraintMustBeSpecified = constraintMustBeSpecified(dto, rule);
        if (!constraintMustBeSpecified.isSuccess) return constraintMustBeSpecified;
        switch (dto.getType())
        {
            case EQUAL_TO:
                return new EqualToConstraintValidator(rule, fields).validate((EqualToConstraintDTO) dto);
            case IN_SET:
                return new InSetConstraintValidator(rule, fields).validate((InSetConstraintDTO) dto);
            case IN_MAP:
                return new InMapConstraintValidator(rule, fields).validate((InMapConstraintDTO) dto);
            case IS_NULL:
                return new IsNullConstraintValidator(rule, fields).validate((IsNullConstraintDTO)dto);
            case GRANULAR_TO:
                return new GranularToConstraintValidator(rule, fields).validate((GranularToConstraintDTO) dto);
            case MATCHES_REGEX:
            case CONTAINS_REGEX:
                return new RegexConstraintValidator(rule, fields).validate((RegexConstraintDTO) dto);
            case EQUAL_TO_FIELD:
            case GREATER_THAN_FIELD:
            case GREATER_THAN_OR_EQUAL_TO_FIELD:
            case LESS_THAN_FIELD:
            case LESS_THAN_OR_EQUAL_TO_FIELD:
            case AFTER_FIELD:
            case AFTER_OR_AT_FIELD:
            case BEFORE_FIELD:
            case BEFORE_OR_AT_FIELD:
                return new RelationalConstraintValidator<>(rule, fields).validate((RelationalConstraintDTO) dto);
            case OF_LENGTH:
            case LONGER_THAN:
            case SHORTER_THAN:
                return new NumericConstraintValidator(rule, fields, FieldType.STRING).validate((NumericConstraintDTO) dto);
            case GREATER_THAN:
            case GREATER_THAN_OR_EQUAL_TO:
            case LESS_THAN:
            case LESS_THAN_OR_EQUAL_TO:
                return new NumericConstraintValidator(rule, fields, FieldType.NUMERIC).validate((NumericConstraintDTO) dto);
            case AFTER:
            case AFTER_OR_AT:
            case BEFORE:
            case BEFORE_OR_AT:
                return new TemporalConstraintValidator(rule, fields).validate((TemporalConstraintDTO) dto);
            case NOT:
                return new NotConstraintValidator(rule, fields).validate((NotConstraintDTO) dto);
            case ANY_OF:
                return new AnyOfConstraintValidator(rule, fields).validate((AnyOfConstraintDTO) dto);
            case ALL_OF:
                return new AllOfConstraintValidator(rule, fields).validate((AllOfConstraintDTO) dto);
            case IF:
                return new ConditionalConstraintValidator(rule, fields).validate((ConditionalConstraintDTO) dto);
            default:
                throw new IllegalStateException("Unexpected constraint type: " + dto.getType());
        }
    }

    private static ValidationResult constraintMustBeSpecified(ConstraintDTO dto, String rule)
    {
        if(dto == null)
        {
            return  ValidationResult.failure("Constraint must not be null | Rule: " + rule);
        }
        if(dto instanceof InvalidConstraintDTO)
        {
            return ValidationResult.failure("Invalid json: " + ((InvalidConstraintDTO)dto).json + " | Rule: " + rule);
        }
        if(dto.getType() == null)
        {
            return ValidationResult.failure("Constraint type must not be null | Constraint: " + dto.getName() + " | Rule: " + rule);
        }
        return ValidationResult.success();
    }

    protected ValidationResult validateGranularity(T dto, String field, Object value)
    {
        FieldType fieldType = fields.stream().filter(f -> f.name.equals(field)).findFirst().get().type.getFieldType();

        if(fieldType == FieldType.STRING)
        {
            return ValidationResult.failure("Granularity "+value+" is not supported for string fields" + getErrorInfo(dto));
        }
        if (fieldType == FieldType.DATETIME)
        {
            return new DateTimeGranularityValidator(getErrorInfo(dto)).validate((String) value);
        }

        return ValidationResult.success();
    }
}
