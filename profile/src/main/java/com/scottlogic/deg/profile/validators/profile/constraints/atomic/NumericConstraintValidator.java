/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottlogic.deg.profile.validators.profile.constraints.atomic;


import com.scottlogic.deg.common.profile.FieldType;
import com.scottlogic.deg.common.util.NumberUtils;
import com.scottlogic.deg.common.util.defaults.NumericDefaults;
import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.profile.dtos.FieldDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.numeric.NumericConstraintDTO;

import java.math.BigDecimal;
import java.util.List;

public class NumericConstraintValidator extends AtomicConstraintValidator<NumericConstraintDTO>
{
    private final FieldType expectedFieldType;

    public NumericConstraintValidator(String rule, List<FieldDTO> fields, FieldType expectedFieldType)
    {
        super(rule, fields);
        this.expectedFieldType = expectedFieldType;
    }

    @Override
    public final ValidationResult validate(NumericConstraintDTO dto)
    {
        ValidationResult fieldMustBeValid = fieldMustBeValid(dto);
        if(!fieldMustBeValid.isSuccess) return fieldMustBeValid;

        ValidationResult numberMustBeValid = numberMustBeValid(dto);
        if(!numberMustBeValid.isSuccess) return numberMustBeValid;

        return fieldTypeMustMatchValueType(dto, expectedFieldType);
    }

    private ValidationResult numberMustBeValid(NumericConstraintDTO dto)
    {
        if (dto.getNumber() == null)
        {
            return ValidationResult.failure("Number must be specified" + getErrorInfo(dto));
        }
        BigDecimal number = NumberUtils.coerceToBigDecimal(dto.getNumber());
        if(number == null)
        {
            return ValidationResult.failure("Number cannot be converted to big decimal" + getErrorInfo(dto));
        }
        BigDecimal min = NumericDefaults.get().min();
        if (number.compareTo(min) < 0)
        {
            return ValidationResult.failure(String.format("Number must have a value >= %s, currently is %s", min.toPlainString(), number.toPlainString()));
        }
        BigDecimal max = NumericDefaults.get().max();
        if (number.compareTo(max) > 0)
        {
            return ValidationResult.failure(String.format("Number must have a value <= %s, currently is %s", max.toPlainString(), number.toPlainString()));
        }
        return ValidationResult.success();
    }
}
