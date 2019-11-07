package com.scottlogic.deg.profile.validators.profile.constraints.atomic;

import com.scottlogic.deg.common.profile.FieldType;
import com.scottlogic.deg.common.util.Defaults;
import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.profile.dtos.FieldDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.integer.IntegerConstraintDTO;

import java.math.BigDecimal;
import java.util.List;

public class IntegerConstraintValidator extends AtomicConstraintValidator<IntegerConstraintDTO>
{
    private final FieldType expectedFieldType;

    public IntegerConstraintValidator(String rule, List<FieldDTO> fields, FieldType expectedFieldType)
    {
        super(rule, fields);
        this.expectedFieldType = expectedFieldType;
    }

    @Override
    public final ValidationResult validate(IntegerConstraintDTO dto)
    {
        ValidationResult fieldMustBeValid = fieldMustBeValid(dto);
        if(!fieldMustBeValid.isSuccess) return fieldMustBeValid;

        ValidationResult integerMustBeValid = integerMustBeValid(dto);
        if(!integerMustBeValid.isSuccess) return integerMustBeValid;

        return fieldTypeMustMatchValueType(dto, expectedFieldType);
    }

    private ValidationResult integerMustBeValid(IntegerConstraintDTO dto)
    {
        BigDecimal integer = BigDecimal.valueOf(dto.getInt());
        BigDecimal max = BigDecimal.valueOf(Defaults.MAX_STRING_LENGTH);
        BigDecimal min = BigDecimal.ZERO;
        if (integer.compareTo(min) < 0)
        {
            return ValidationResult.failure(String.format("String length must have a value >= %s, currently is %s", min.toPlainString(), integer.toPlainString()));
        }
        if (integer.compareTo(max) > 0)
        {
            return ValidationResult.failure(String.format("String length must have a value <= %s, currently is %s", max.toPlainString(), integer.toPlainString()));
        }
        return ValidationResult.success();
    }
}
