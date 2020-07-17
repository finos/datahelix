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

import com.scottlogic.datahelix.generator.common.profile.StandardSpecificFieldType;
import com.scottlogic.datahelix.generator.common.validators.ValidationResult;
import com.scottlogic.datahelix.generator.profile.creation.FieldDTOBuilder;
import com.scottlogic.datahelix.generator.profile.dtos.FieldDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.EqualToConstraintDTO;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.scottlogic.datahelix.generator.profile.creation.AtomicConstraintDTOBuilder.atomicConstraintDTO;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class EqualToConstraintValidatorTests
{

    private final List<FieldDTO> fields = Arrays.asList
        (
            FieldDTOBuilder.fieldDTO("text", StandardSpecificFieldType.STRING).build()
        );

    @Test
    public void validateEqualToConstraint_withValidData_succeeds()
    {
        // Arrange
        EqualToConstraintDTO dto = atomicConstraintDTO("text").buildEqualTo("test");

        // Act
        ValidationResult validationResult = new EqualToConstraintValidator(fields).validate(dto);

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateEqualToConstraint_withNullField_fails()
    {
        // Arrange
        EqualToConstraintDTO dto = atomicConstraintDTO(null).buildEqualTo("test");

        // Act
        ValidationResult validationResult = new EqualToConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Field must be specified | Field: NULL | Constraint: 'equalTo'"));
    }

    @Test
    public void validateEqualToConstraint_withEmptyField_fails()
    {
        // Arrange
        EqualToConstraintDTO dto = atomicConstraintDTO("").buildEqualTo("test");

        // Act
        ValidationResult validationResult = new EqualToConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Field must be specified | Field: '' | Constraint: 'equalTo'"));
    }

    @Test
    public void validateEqualToConstraint_withUndefinedField_fails()
    {
        // Arrange
        EqualToConstraintDTO dto = atomicConstraintDTO("unknown").buildEqualTo("test");

        // Act
        ValidationResult validationResult = new EqualToConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("'unknown' must be defined in fields | Field: 'unknown' | Constraint: 'equalTo'"));
    }

    @Test
    public void validateEqualToConstraint_withInvalidData_fails()
    {
        // Arrange
        EqualToConstraintDTO dto = atomicConstraintDTO("text").buildEqualTo(true);

        // Act
        ValidationResult validationResult = new EqualToConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Value true must be a string | Field: 'text' | Constraint: 'equalTo'"));
    }
}
