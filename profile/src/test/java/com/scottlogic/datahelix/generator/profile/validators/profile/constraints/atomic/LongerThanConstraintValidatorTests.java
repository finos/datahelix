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
package com.scottlogic.datahelix.generator.profile.validators.profile.constraints.atomic;

import com.scottlogic.datahelix.generator.common.profile.FieldType;
import com.scottlogic.datahelix.generator.common.profile.StandardSpecificFieldType;
import com.scottlogic.datahelix.generator.common.util.Defaults;
import com.scottlogic.datahelix.generator.common.validators.ValidationResult;
import com.scottlogic.datahelix.generator.profile.creation.FieldDTOBuilder;
import com.scottlogic.datahelix.generator.profile.dtos.FieldDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.integer.LongerThanConstraintDTO;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LongerThanConstraintValidatorTests {

    private final List<FieldDTO> fields = Arrays.asList
        (
            FieldDTOBuilder.create("integer", StandardSpecificFieldType.INTEGER.toSpecificFieldType())
        );

    @Test
    public void validateLongerThanConstraint_withValidData_succeeds()
    {
        // Arrange
        LongerThanConstraintDTO dto = new LongerThanConstraintDTO();
        dto.field = "integer";
        dto.value = 1;

        // Act
        ValidationResult validationResult = new LongerThanConstraintValidator(fields, FieldType.NUMERIC).validate(dto);

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateLongerThanConstraint_withNullField_fails()
    {
        // Arrange
        LongerThanConstraintDTO dto = new LongerThanConstraintDTO();
        dto.field = null;
        dto.value = 1;

        // Act
        ValidationResult validationResult = new LongerThanConstraintValidator(fields, FieldType.NUMERIC).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateLongerThanConstraint_withEmptyField_fails()
    {
        // Arrange
        LongerThanConstraintDTO dto = new LongerThanConstraintDTO();
        dto.field = "";
        dto.value = 1;

        // Act
        ValidationResult validationResult = new LongerThanConstraintValidator(fields, FieldType.NUMERIC).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateLongerThanConstraint_withUndefinedField_fails()
    {
        // Arrange
        LongerThanConstraintDTO dto = new LongerThanConstraintDTO();
        dto.field = "unknown";
        dto.value = 1;

        // Act
        ValidationResult validationResult = new LongerThanConstraintValidator(fields, FieldType.NUMERIC).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateLongerThanConstraint_withValueTooLarge_fails() {
        // Arrange
        LongerThanConstraintDTO dto = new LongerThanConstraintDTO();
        dto.field = "integer";
        dto.value = Defaults.MAX_STRING_LENGTH + 1;

        // Act
        ValidationResult validationResult = new LongerThanConstraintValidator(fields, FieldType.NUMERIC).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }
}
