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
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.integer.OfLengthConstraintDTO;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.scottlogic.datahelix.generator.profile.creation.AtomicConstraintDTOBuilder.atomicConstraintDTO;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class OfLengthConstraintValidatorTests
{

    private final List<FieldDTO> fields = Arrays.asList
        (
            FieldDTOBuilder.fieldDTO("text", StandardSpecificFieldType.STRING).build(),
            FieldDTOBuilder.fieldDTO("decimal", StandardSpecificFieldType.DECIMAL).build()
        );

    @Test
    public void validateOfLengthConstraint_withValidData_succeeds()
    {
        // Arrange
        OfLengthConstraintDTO dto = atomicConstraintDTO("text").buildOfLength(1);

        // Act
        ValidationResult validationResult = new OfLengthConstraintValidator(fields, FieldType.STRING).validate(dto);

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateOfLengthConstraint_withNullField_fails()
    {
        // Arrange
        OfLengthConstraintDTO dto = atomicConstraintDTO(null).buildOfLength(1);

        // Act
        ValidationResult validationResult = new OfLengthConstraintValidator(fields, FieldType.STRING).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Field must be specified | Field: NULL | Constraint: 'ofLength'"));
    }

    @Test
    public void validateOfLengthConstraint_withEmptyField_fails()
    {
        // Arrange
        OfLengthConstraintDTO dto = atomicConstraintDTO("").buildOfLength(1);

        // Act
        ValidationResult validationResult = new OfLengthConstraintValidator(fields, FieldType.STRING).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Field must be specified | Field: '' | Constraint: 'ofLength'"));
    }

    @Test
    public void validateOfLengthConstraint_withUndefinedField_fails()
    {
        // Arrange
        OfLengthConstraintDTO dto = atomicConstraintDTO("unknown").buildOfLength(1);

        // Act
        ValidationResult validationResult = new OfLengthConstraintValidator(fields, FieldType.STRING).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("'unknown' must be defined in fields | Field: 'unknown' | Constraint: 'ofLength'"));
    }

    @Test
    public void validateOfLengthConstraint_withValueLessThanMin_fails()
    {
        // Arrange
        OfLengthConstraintDTO dto = atomicConstraintDTO("text").buildOfLength(-1);

        // Act
        ValidationResult validationResult = new OfLengthConstraintValidator(fields, FieldType.STRING).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("String length must have a value >= 0, currently is -1"));
    }

    @Test
    public void validateOfLengthConstraint_withValueGreaterThanMax_fails()
    {
        // Arrange
        OfLengthConstraintDTO dto = atomicConstraintDTO("text").buildOfLength(Defaults.MAX_STRING_LENGTH + 1);

        // Act
        ValidationResult validationResult = new OfLengthConstraintValidator(fields, FieldType.STRING).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("String length must have a value <= 1000, currently is 1001"));
    }

    @Test
    public void validateOfLengthConstraint_withInvalidFieldType_fails()
    {
        // Arrange
        OfLengthConstraintDTO dto = atomicConstraintDTO("decimal").buildOfLength(1);

        // Act
        ValidationResult validationResult = new OfLengthConstraintValidator(fields, FieldType.STRING).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Expected field type STRING doesn't match field type NUMERIC | Field: 'decimal' | Constraint: 'ofLength'"));
    }
}
