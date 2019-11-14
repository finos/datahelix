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

import com.scottlogic.datahelix.generator.common.validators.ValidationResult;
import com.scottlogic.datahelix.generator.profile.creation.FieldDTOBuilder;
import com.scottlogic.datahelix.generator.profile.dtos.FieldDTO;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FieldValidatorTests
{

    private final FieldValidator fieldValidator = new FieldValidator();

    @Test
    public void validateField_withValidData_succeeds()
    {
        // Arrange
        FieldDTO dto = FieldDTOBuilder.create("test");

        // Act
        ValidationResult validationResult = fieldValidator.validate(dto);

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateField_withNullField_fails()
    {
        // Arrange
        FieldDTO dto = null;

        // Act
        ValidationResult validationResult = fieldValidator.validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }
    @Test
    public void validateField_withNullName_fails()
    {
        // Arrange
        FieldDTO dto = FieldDTOBuilder.create(null);

        // Act
        ValidationResult validationResult = fieldValidator.validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateField_withEmptyName_fails()
    {
        // Arrange
        FieldDTO dto = FieldDTOBuilder.create("");

        // Act
        ValidationResult validationResult = fieldValidator.validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateField_withNullType_fails()
    {
        // Arrange
        FieldDTO dto = FieldDTOBuilder.create("test", null);

        // Act
        ValidationResult validationResult = fieldValidator.validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }
}