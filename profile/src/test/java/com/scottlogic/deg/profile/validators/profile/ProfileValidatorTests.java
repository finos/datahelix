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

import com.scottlogic.datahelix.generator.common.validators.ValidationResult;
import com.scottlogic.deg.profile.creation.FieldDTOBuilder;
import com.scottlogic.deg.profile.dtos.ProfileDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ProfileValidatorTests
{
    private final ProfileValidator profileValidator = new ProfileValidator(null);

    @Test
    public void validateProfile_withValidData_succeeds()
    {
        // Arrange
        ProfileDTO dto = new ProfileDTO();
        dto.fields = Collections.singletonList(FieldDTOBuilder.create("test"));
        dto.rules = new ArrayList<>();

        // Act
        ValidationResult validationResult = profileValidator.validate(dto);

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateProfile_withNullFields_fails()
    {
        // Arrange
        ProfileDTO dto = new ProfileDTO();
        dto.fields = null;
        dto.rules = new ArrayList<>();

        // Act
        ValidationResult validationResult = profileValidator.validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateProfile_withEmptyFields_fails()
    {
        // Arrange
        ProfileDTO dto = new ProfileDTO();
        dto.fields = new ArrayList<>();
        dto.rules = new ArrayList<>();

        // Act
        ValidationResult validationResult = profileValidator.validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateProfile_withNonUniqueFields_fails()
    {
        // Arrange
        ProfileDTO dto = new ProfileDTO();
        dto.fields = Arrays.asList(FieldDTOBuilder.create("test"), FieldDTOBuilder.create("test"));
        dto.rules = new ArrayList<>();

        // Act
        ValidationResult validationResult = profileValidator.validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateProfile_withNullRules_fails()
    {
        // Arrange
        ProfileDTO dto = new ProfileDTO();
        dto.fields = Collections.singletonList(FieldDTOBuilder.create("test"));
        dto.rules = null;

        // Act
        ValidationResult validationResult = profileValidator.validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }
}