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

package com.scottlogic.datahelix.generator.profile.validators.profile;

import com.scottlogic.datahelix.generator.common.profile.SpecificFieldType;
import com.scottlogic.datahelix.generator.common.validators.ValidationResult;
import com.scottlogic.datahelix.generator.common.validators.Validator;
import com.scottlogic.datahelix.generator.profile.dtos.FieldDTO;
import com.scottlogic.datahelix.generator.profile.services.FieldService;

import java.util.Optional;

public class FieldValidator implements Validator<FieldDTO>
{
    public static SpecificFieldType getSpecificFieldType(FieldDTO field)
    {
        return extractSpecificFieldType(field)
            .orElseThrow(() -> new IllegalStateException(String.format("Invalid field type occurred: '%s'", field.type)));
    }

    private static Optional<SpecificFieldType> extractSpecificFieldType(FieldDTO field)
    {
        return FieldService.specificFieldTypeFromString(field.type, field.formatting);
    }

    @Override
    public ValidationResult validate(FieldDTO field)
    {
        ValidationResult fieldMustBeSpecified = fieldMustBeSpecified(field);
        if (!fieldMustBeSpecified.isSuccess) return fieldMustBeSpecified;
        return ValidationResult.combine(nameMustBeSpecified(field), typeMustBeValid(field));
    }

    private ValidationResult fieldMustBeSpecified(FieldDTO field)
    {
        return field != null
            ? ValidationResult.success()
            : ValidationResult.failure("Field must not be null");
    }

    private ValidationResult nameMustBeSpecified(FieldDTO field)
    {
        return field.name != null && !field.name.isEmpty()
            ? ValidationResult.success()
            : ValidationResult.failure("Field name must be specified");
    }

    private ValidationResult typeMustBeValid(FieldDTO field)
    {
        ValidationResult typeMustBeSpecified = typeMustBeSpecified(field);
        if (!typeMustBeSpecified.isSuccess) return typeMustBeSpecified;

        return typeMustBeSupported(field);
    }

    private ValidationResult typeMustBeSpecified(FieldDTO field)
    {
        String fieldName = field.name == null ? "Unnamed" : ValidationResult.quote(field.name);
        return field.type != null
            ? ValidationResult.success()
            : ValidationResult.failure(String.format("Field type must be specified | Field: %s", fieldName));
    }

    private ValidationResult typeMustBeSupported(FieldDTO field)
    {
        return extractSpecificFieldType(field).map(t -> ValidationResult.success())
            .orElse(ValidationResult.failure(String.format("Field type %s is not supported | Field: %s", ValidationResult.quote(field.type), ValidationResult.quote(field.name))));
    }
}

