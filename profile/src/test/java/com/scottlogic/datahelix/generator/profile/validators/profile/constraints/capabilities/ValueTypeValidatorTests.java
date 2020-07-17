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
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ValueTypeValidatorTests
{
    public static final String ERROR_INFO = " | error info";

    @Test
    public void validateValueType_withStringTypeAndValidData_succeeds()
    {
        // Act
        ValidationResult validationResult = new ValueTypeValidator(FieldType.STRING, ERROR_INFO).validate("test");

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateValueType_withStringTypeAndNumericValue_fails()
    {
        // Act
        ValidationResult validationResult = new ValueTypeValidator(FieldType.STRING, ERROR_INFO).validate(1.1);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Value 1.1 must be a string | error info"));
    }

    @Test
    public void validateValueType_withStringTypeAndBooleanValue_fails()
    {
        // Act
        ValidationResult validationResult = new ValueTypeValidator(FieldType.STRING, ERROR_INFO).validate(true);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Value true must be a string | error info"));
    }

    @Test
    public void validateValueType_withNumericTypeAndValidData_succeeds()
    {
        // Act
        ValidationResult validationResult = new ValueTypeValidator(FieldType.NUMERIC, ERROR_INFO).validate(1.1);

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateValueType_withNumericTypeAndStringValue_fails()
    {
        // Act
        ValidationResult validationResult = new ValueTypeValidator(FieldType.NUMERIC, ERROR_INFO).validate("text");

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Value 'text' must be a number | error info"));
    }

    @Test
    public void validateValueType_withNumericTypeAndBooleanValue_fails()
    {
        // Act
        ValidationResult validationResult = new ValueTypeValidator(FieldType.NUMERIC, ERROR_INFO).validate(true);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Value true must be a number | error info"));
    }

    @Test
    public void validateValueType_withBooleanTypeAndValidData_succeeds()
    {
        // Act
        ValidationResult validationResult = new ValueTypeValidator(FieldType.BOOLEAN, ERROR_INFO).validate(true);

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateValueType_withBooleanTypeAndNumericValue_fails()
    {
        // Act
        ValidationResult validationResult = new ValueTypeValidator(FieldType.BOOLEAN, ERROR_INFO).validate(1.1);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Value 1.1 must be a boolean | error info"));
    }

    @Test
    public void validateValueType_withBooleanTypeAndStringValue_fails()
    {
        // Act
        ValidationResult validationResult = new ValueTypeValidator(FieldType.BOOLEAN, ERROR_INFO).validate("text");

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Value 'text' must be a boolean | error info"));
    }
}
