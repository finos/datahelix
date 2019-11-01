package com.scottlogic.deg.profile.creation.validators.constraints.atomic;

import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.profile.creation.dtos.FieldDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.atomic.AtomicConstraintDTO;
import com.scottlogic.deg.profile.creation.validators.constraints.ConstraintValidator;

import java.util.List;

public abstract class AtomicConstraintValidator<TAtomicConstraintDTO extends AtomicConstraintDTO> extends ConstraintValidator<TAtomicConstraintDTO>
{
    protected AtomicConstraintValidator(List<FieldDTO> fields, String rule)
    {
        super(fields, rule);
    }

    @Override
    public ValidationResult validate(TAtomicConstraintDTO atomicConstraint)
    {
        ValidationResult baseValidationResult = super.validate(atomicConstraint);
        if(!baseValidationResult.isValid) return baseValidationResult;
        return fieldMustBeValid(atomicConstraint);
    }

    private ValidationResult fieldMustBeValid(TAtomicConstraintDTO atomicConstraint)
    {
        if(atomicConstraint.field == null || atomicConstraint.field.isEmpty())
        {
            return ValidationResult.failure("Atomic constraint's referenced field must be specified");
        }
        if(fields.stream().noneMatch(field -> field.name.equals(atomicConstraint.field)))
        {
            return ValidationResult.failure("Atomic constraint's referenced field "
                +atomicConstraint.field+" must be defined in fields | Rule: " + rule);
        }
        return ValidationResult.success();
    }
}
