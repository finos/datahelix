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

package com.scottlogic.datahelix.generator.profile.validators.profile.constraints.capabilities;

import com.scottlogic.datahelix.generator.common.profile.FieldType;
import com.scottlogic.datahelix.generator.common.validators.ValidationResult;
import com.scottlogic.datahelix.generator.common.validators.Validator;
import com.scottlogic.datahelix.generator.common.whitelist.WeightedElement;

public class ValueTypeValidator implements Validator<Object>
{
    private final FieldType expectedFieldType;
    private final String errorInfo;

    public ValueTypeValidator(FieldType expectedFieldType, String errorInfo)
    {
        this.expectedFieldType = expectedFieldType;
        this.errorInfo = errorInfo;
    }

    @Override
    public final ValidationResult validate(Object value)
    {
        if (value == null) {
            return ValidationResult.failure("Values must be specified" + errorInfo);
        }
        if (value instanceof WeightedElement) {
            return validate(((WeightedElement) value).getElement());
        }

        switch (expectedFieldType) {
            case BOOLEAN:
                return value instanceof Boolean
                    ? ValidationResult.success()
                    : ValidationResult.failure(String.format("Value %s must be a boolean%s", ValidationResult.quote(value), errorInfo));
            case NUMERIC:
                return value instanceof Number || value instanceof String && isNumber((String) value)
                    ? ValidationResult.success()
                    : ValidationResult.failure(String.format("Value %s must be a number%s", ValidationResult.quote(value), errorInfo));
            default:
                return value instanceof String ? ValidationResult.success() : ValidationResult.failure(String.format("Value %s must be a string%s", ValidationResult.quote(value), errorInfo));
        }
    }

    private static boolean isNumber(String s)
    {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }
}
