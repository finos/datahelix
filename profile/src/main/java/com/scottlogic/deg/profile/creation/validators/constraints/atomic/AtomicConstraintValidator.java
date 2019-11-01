package com.scottlogic.deg.profile.creation.validators.constraints.atomic;

import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.profile.creation.dtos.FieldDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.atomic.AtomicConstraintDTO;
import com.scottlogic.deg.profile.creation.validators.constraints.ConstraintValidator;

import java.util.List;

public abstract class AtomicConstraintValidator<TAtomicConstraintDTO extends AtomicConstraintDTO> extends ConstraintValidator<TAtomicConstraintDTO>
{
    protected AtomicConstraintValidator(String rule, List<FieldDTO> fields)
    {
        super(rule, fields);
    }

    @Override
    public ValidationResult validate(TAtomicConstraintDTO atomicConstraint)
    {
        return fieldMustBeValid(atomicConstraint);
    }


    private ValidationResult fieldMustBeValid(TAtomicConstraintDTO atomicConstraint)
    {
        String field = atomicConstraint.field;
        if (field == null || field.isEmpty())
        {
            return ValidationResult.failure("Field must be specified" + getErrorInfo(atomicConstraint));
        }
        if (fields.stream().noneMatch(f -> f.name.equals(atomicConstraint.field)))
        {
            return ValidationResult.failure("Field "+ field + " must be defined in fields" + getErrorInfo(atomicConstraint));
        }
        return ValidationResult.success();
    }
}
