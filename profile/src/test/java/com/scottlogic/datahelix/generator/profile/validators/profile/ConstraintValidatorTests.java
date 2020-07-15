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

import com.scottlogic.datahelix.generator.common.profile.StandardSpecificFieldType;
import com.scottlogic.datahelix.generator.common.validators.ValidationResult;
import com.scottlogic.datahelix.generator.profile.creation.FieldDTOBuilder;
import com.scottlogic.datahelix.generator.profile.dtos.FieldDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.ConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.InvalidConstraintDTO;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.scottlogic.datahelix.generator.profile.creation.AtomicConstraintDTOBuilder.atomicConstraintDTO;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ConstraintValidatorTests
{
    private final List<FieldDTO> fields = Arrays.asList
        (
            FieldDTOBuilder.fieldDTO("text", StandardSpecificFieldType.STRING).build(),
            FieldDTOBuilder.fieldDTO("integer", StandardSpecificFieldType.INTEGER).build()
        );

    @Test
    public void validateConstraint_withValidData_succeeds()
    {
        // Arrange
        ConstraintDTO dto = atomicConstraintDTO("text").buildEqualTo("test");
        // Act
        ValidationResult validationResult = ConstraintValidator.validateConstraint(dto, fields);

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateConstraint_withNullConstraint_fails()
    {
        // Arrange
        ConstraintDTO dto = null;

        // Act
        ValidationResult validationResult = ConstraintValidator.validateConstraint(dto, fields);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Constraint must not be null"));
    }

    @Test
    public void validateAtomicConstraint_withNullField_fails()
    {
        // Arrange
        ConstraintDTO dto = atomicConstraintDTO(null).buildEqualTo("value");

        // Act
        ValidationResult validationResult = ConstraintValidator.validateConstraint(dto, fields);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Field must be specified | Field: null | Constraint: equalTo"));
    }

    @Test
    public void validateAtomicConstraint_withEmptyField_fails()
    {
        // Arrange
        ConstraintDTO dto = atomicConstraintDTO("").buildEqualTo("value");

        // Act
        ValidationResult validationResult = ConstraintValidator.validateConstraint(dto, fields);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Field must be specified | Field:  | Constraint: equalTo"));
    }

    @Test
    public void validateAtomicConstraint_withUndefinedField_fails()
    {
        // Arrange
        ConstraintDTO dto = atomicConstraintDTO("unknown").buildEqualTo("value");

        // Act
        ValidationResult validationResult = ConstraintValidator.validateConstraint(dto, fields);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("unknown must be defined in fields | Field: unknown | Constraint: equalTo"));
    }

    @Test
    public void validateAtomicConstraint_withNullValue_fails()
    {
        // Arrange
        ConstraintDTO dto = atomicConstraintDTO("text").buildEqualTo(null);

        // Act
        ValidationResult validationResult = ConstraintValidator.validateConstraint(dto, fields);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Values must be specified | Field: text | Constraint: equalTo"));
    }

    @Test
    public void validateAtomicConstraint_withIncorrectValueType_fails()
    {
        // Arrange
        ConstraintDTO dto = atomicConstraintDTO("text").buildEqualTo(1);

        // Act
        ValidationResult validationResult = ConstraintValidator.validateConstraint(dto, fields);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Value 1 must be a string | Field: text | Constraint: equalTo"));
    }

    @Test
    public void validateAtomicConstraint_withInvalidConstraint_fails()
    {
        // Arrange
        ConstraintDTO dto = new InvalidConstraintDTO("{ \"some\": \"json\" }");

        // Act
        ValidationResult validationResult = ConstraintValidator.validateConstraint(dto, fields);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Invalid json: { \"some\": \"json\" }"));
    }
}