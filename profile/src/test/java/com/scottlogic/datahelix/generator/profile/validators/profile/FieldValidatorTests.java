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

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class FieldValidatorTests
{
    private final FieldValidator fieldValidator = new FieldValidator();

    @Test
    public void validateField_withValidData_succeeds()
    {
        // Arrange
        FieldDTO dto = FieldDTOBuilder.fieldDTOWithStringType("test").build();

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
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Field must not be null"));
    }

    @Test
    public void validateField_withNullName_fails()
    {
        // Arrange
        FieldDTO dto = FieldDTOBuilder.fieldDTOWithStringType(null).build();

        // Act
        ValidationResult validationResult = fieldValidator.validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Field name must be specified"));
    }

    @Test
    public void validateField_withEmptyName_fails()
    {
        // Arrange
        FieldDTO dto = FieldDTOBuilder.fieldDTOWithStringType("").build();

        // Act
        ValidationResult validationResult = fieldValidator.validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Field name must be specified"));
    }

    @Test
    public void validateField_withNullType_fails()
    {
        // Arrange
        FieldDTO dto = FieldDTOBuilder.fieldDTO("test", null).build();

        // Act
        ValidationResult validationResult = fieldValidator.validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Field type must be specified | Field: 'test'"));
    }

    @Test
    public void validateField_withNullNameAndNullType_fails()
    {
        // Arrange
        FieldDTO dto = FieldDTOBuilder.fieldDTO(null, null).build();

        // Act
        ValidationResult validationResult = fieldValidator.validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(2));
        assertThat(validationResult.errors, hasItem("Field name must be specified"));
        assertThat(validationResult.errors, hasItem("Field type must be specified | Field: Unnamed"));
    }
}