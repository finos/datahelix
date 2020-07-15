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
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.GranularToConstraintDTO;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.scottlogic.datahelix.generator.profile.creation.AtomicConstraintDTOBuilder.atomicConstraintDTO;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class GranularToConstraintValidatorTests
{

    private final List<FieldDTO> fields = Arrays.asList
        (
            FieldDTOBuilder.fieldDTO("text", StandardSpecificFieldType.STRING).build(),
            FieldDTOBuilder.fieldDTO("decimal", StandardSpecificFieldType.DECIMAL).build(),
            FieldDTOBuilder.fieldDTO("boolean", StandardSpecificFieldType.BOOLEAN).build()
        );

    @Test
    public void validateGranularToConstraint_withValidNumericData_succeeds()
    {
        // Arrange
        GranularToConstraintDTO dto = atomicConstraintDTO("decimal").buildGranularTo(0.1);

        // Act
        ValidationResult validationResult = new GranularToConstraintValidator(fields).validate(dto);

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateGranularToConstraint_withNullField_fails()
    {
        // Arrange
        GranularToConstraintDTO dto = atomicConstraintDTO(null).buildGranularTo(0.1);

        // Act
        ValidationResult validationResult = new GranularToConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Field must be specified | Field: NULL | Constraint: 'granularTo'"));
    }

    @Test
    public void validateGranularToConstraint_withEmptyField_fails()
    {
        // Arrange
        GranularToConstraintDTO dto = atomicConstraintDTO("").buildGranularTo(0.1);

        // Act
        ValidationResult validationResult = new GranularToConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Field must be specified | Field: '' | Constraint: 'granularTo'"));
    }

    @Test
    public void validateGranularToConstraint_withUndefinedField_fails()
    {
        // Arrange
        GranularToConstraintDTO dto = atomicConstraintDTO("unknown").buildGranularTo(0.1);

        // Act
        ValidationResult validationResult = new GranularToConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("'unknown' must be defined in fields | Field: 'unknown' | Constraint: 'granularTo'"));
    }

    @Test
    public void validateGranularToConstraint_withValueTypeBoolean_fails()
    {
        // Arrange
        GranularToConstraintDTO dto = atomicConstraintDTO("boolean").buildGranularTo(true);

        // Act
        ValidationResult validationResult = new GranularToConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Granularity true is not supported for boolean fields | Field: 'boolean' | Constraint: 'granularTo'"));
    }

    @Test
    public void validateGranularToConstraint_withValueTypeString_fails()
    {
        // Arrange
        GranularToConstraintDTO dto = atomicConstraintDTO("text").buildGranularTo("test");

        // Act
        ValidationResult validationResult = new GranularToConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Granularity 'test' is not supported for string fields | Field: 'text' | Constraint: 'granularTo'"));
    }
}
