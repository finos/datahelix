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
package com.scottlogic.deg.common.validators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ValidationResult
{
    public final boolean isValid;
    public final List<String> errors;

    private ValidationResult(boolean isValid, List<String> errors)
    {
        this.isValid = isValid;
        this.errors = errors;
    }

    public static ValidationResult success(){
        return new ValidationResult(true, new ArrayList<>());
    }

    public static ValidationResult failure(String... errors)
    {
        return new ValidationResult(false, Arrays.stream(errors).collect(Collectors.toList()));
    }

    public static ValidationResult failure(List<String> errors)
    {
        return new ValidationResult(false, errors);
    }

    public static ValidationResult combine(List<ValidationResult> validationResults)
    {
        boolean isValid = validationResults.stream()
            .allMatch(validationResult -> validationResult.isValid);
        List<String> errors = validationResults.stream()
            .flatMap(validationResult -> validationResult.errors.stream())
            .collect(Collectors.toList());
        return new ValidationResult(isValid, errors);
    }

    public static ValidationResult combine(ValidationResult... validationResults)
    {
        boolean isValid = Arrays.stream(validationResults)
            .allMatch(validationResult -> validationResult.isValid);
        List<String> errors = Arrays.stream(validationResults)
            .flatMap(validationResult -> validationResult.errors.stream())
            .collect(Collectors.toList());
        return new ValidationResult(isValid, errors);
    }
}


