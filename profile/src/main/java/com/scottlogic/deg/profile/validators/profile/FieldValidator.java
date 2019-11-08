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
package com.scottlogic.deg.profile.validators.profile;

import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.common.validators.Validator;
import com.scottlogic.deg.profile.dtos.FieldDTO;

public class FieldValidator implements Validator<FieldDTO>
{
    @Override
    public ValidationResult validate(FieldDTO field)
    {
        ValidationResult fieldMustBeSpecified = fieldMustBeSpecified(field);
        if(!fieldMustBeSpecified.isSuccess) return fieldMustBeSpecified;
        return ValidationResult.combine(nameMustBeSpecified(field), typeMustBeSpecified(field));
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

    private ValidationResult typeMustBeSpecified(FieldDTO field)
    {
        String fieldName = field.name == null ? "Unnamed field" : field.name;
        return field.type != null
            ? ValidationResult.success()
            : ValidationResult.failure("Field type must be specified | Field: " + fieldName);
    }


}

