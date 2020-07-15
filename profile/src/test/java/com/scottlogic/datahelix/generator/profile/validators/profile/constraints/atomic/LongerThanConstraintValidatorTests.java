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

import static com.scottlogic.datahelix.generator.profile.creation.AtomicConstraintDTOBuilder.atomicConstraintDTO;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class LongerThanConstraintValidatorTests
{

    private final List<FieldDTO> fields = Arrays.asList
        (
            FieldDTOBuilder.fieldDTO("text", StandardSpecificFieldType.STRING).build(),
            FieldDTOBuilder.fieldDTO("decimal", StandardSpecificFieldType.DECIMAL).build()
        );

    @Test
    public void validateLongerThanConstraint_withValidData_succeeds()
    {
        // Arrange
        LongerThanConstraintDTO dto = atomicConstraintDTO("text").buildLongerThan(1);

        // Act
        ValidationResult validationResult = new LongerThanConstraintValidator(fields, FieldType.STRING).validate(dto);

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateLongerThanConstraint_withNullField_fails()
    {
        // Arrange
        LongerThanConstraintDTO dto = atomicConstraintDTO(null).buildLongerThan(1);

        // Act
        ValidationResult validationResult = new LongerThanConstraintValidator(fields, FieldType.STRING).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Field must be specified | Field: null | Constraint: longerThan"));
    }

    @Test
    public void validateLongerThanConstraint_withEmptyField_fails()
    {
        // Arrange
        LongerThanConstraintDTO dto = atomicConstraintDTO("").buildLongerThan(1);

        // Act
        ValidationResult validationResult = new LongerThanConstraintValidator(fields, FieldType.STRING).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Field must be specified | Field:  | Constraint: longerThan"));
    }

    @Test
    public void validateLongerThanConstraint_withUndefinedField_fails()
    {
        // Arrange
        LongerThanConstraintDTO dto = atomicConstraintDTO("unknown").buildLongerThan(1);

        // Act
        ValidationResult validationResult = new LongerThanConstraintValidator(fields, FieldType.STRING).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("unknown must be defined in fields | Field: unknown | Constraint: longerThan"));
    }

    @Test
    public void validateLongerThanConstraint_withValueTooLarge_fails()
    {
        // Arrange
        LongerThanConstraintDTO dto = atomicConstraintDTO("text").buildLongerThan(Defaults.MAX_STRING_LENGTH + 1);

        // Act
        ValidationResult validationResult = new LongerThanConstraintValidator(fields, FieldType.STRING).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("String length must have a value <= 1000, currently is 1001"));
    }

    @Test
    public void validateLongerThanConstraint_withNegativeValue_succeeds()
    {
        // Arrange
        LongerThanConstraintDTO dto = atomicConstraintDTO("text").buildLongerThan(-11);

        // Act
        ValidationResult validationResult = new LongerThanConstraintValidator(fields, FieldType.STRING).validate(dto);

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateLongerThanConstraint_withInvalidFieldType_fails()
    {
        // Arrange
        LongerThanConstraintDTO dto = atomicConstraintDTO("decimal").buildLongerThan(1);

        // Act
        ValidationResult validationResult = new LongerThanConstraintValidator(fields, FieldType.STRING).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Expected field type STRING doesn't match field type NUMERIC | Field: decimal | Constraint: longerThan"));
    }
}
