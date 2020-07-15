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

import com.scottlogic.datahelix.generator.common.profile.StandardSpecificFieldType;
import com.scottlogic.datahelix.generator.common.validators.ValidationResult;
import com.scottlogic.datahelix.generator.profile.creation.FieldDTOBuilder;
import com.scottlogic.datahelix.generator.profile.dtos.FieldDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.EqualToConstraintDTO;
import com.scottlogic.datahelix.generator.profile.validators.profile.constraints.atomic.EqualToConstraintValidator;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.scottlogic.datahelix.generator.profile.creation.AtomicConstraintDTOBuilder.atomicConstraintDTO;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ValueTypeValidatorTests
{
    private final List<FieldDTO> fields = Arrays.asList
        (
            FieldDTOBuilder.fieldDTO("text", StandardSpecificFieldType.STRING).build(),
            FieldDTOBuilder.fieldDTO("decimal", StandardSpecificFieldType.DECIMAL).build(),
            FieldDTOBuilder.fieldDTO("boolean", StandardSpecificFieldType.BOOLEAN).build()
        );

    @Test
    public void validateValueType_withStringTypeAndValidData_succeeds()
    {
        // Arrange
        EqualToConstraintDTO dto = atomicConstraintDTO("text").buildEqualTo("test");

        // Act
        ValidationResult validationResult = new EqualToConstraintValidator(fields).validate(dto);

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateValueType_withStringTypeAndNumericValue_fails()
    {
        // Arrange
        EqualToConstraintDTO dto = atomicConstraintDTO("text").buildEqualTo(1.1);

        // Act
        ValidationResult validationResult = new EqualToConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Value 1.1 must be a string | Field: text | Constraint: equalTo"));
    }

    @Test
    public void validateValueType_withStringTypeAndBooleanValue_fails()
    {
        // Arrange
        EqualToConstraintDTO dto = atomicConstraintDTO("text").buildEqualTo(true);

        // Act
        ValidationResult validationResult = new EqualToConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        // TODO: This error message is not helpful or correct
        assertThat(validationResult.errors, hasItem("Value true must be a boolean | Field: text | Constraint: equalTo"));
    }

    @Test
    public void validateValueType_withNumericTypeAndValidData_succeeds()
    {
        // Arrange
        EqualToConstraintDTO dto = atomicConstraintDTO("decimal").buildEqualTo(1.1);

        // Act
        ValidationResult validationResult = new EqualToConstraintValidator(fields).validate(dto);

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateValueType_withNumericTypeAndStringValue_fails()
    {
        // Arrange
        EqualToConstraintDTO dto = atomicConstraintDTO("decimal").buildEqualTo("text");

        // Act
        ValidationResult validationResult = new EqualToConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        // TODO: This error message does not quote the string
        assertThat(validationResult.errors, hasItem("Value text must be a number | Field: decimal | Constraint: equalTo"));
    }

    @Test
    public void validateValueType_withNumericTypeAndBooleanValue_fails()
    {
        // Arrange
        EqualToConstraintDTO dto = atomicConstraintDTO("decimal").buildEqualTo(true);

        // Act
        ValidationResult validationResult = new EqualToConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        // TODO: This error message is not helpful or correct
        assertThat(validationResult.errors, hasItem("Value true must be a boolean | Field: decimal | Constraint: equalTo"));
    }

    @Test
    public void validateValueType_withBooleanTypeAndValidData_succeeds()
    {
        // Arrange
        EqualToConstraintDTO dto = atomicConstraintDTO("boolean").buildEqualTo(true);

        // Act
        ValidationResult validationResult = new EqualToConstraintValidator(fields).validate(dto);

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateValueType_withBooleanTypeAndNumericValue_fails()
    {
        // Arrange
        EqualToConstraintDTO dto = atomicConstraintDTO("boolean").buildEqualTo(1.1);

        // Act
        ValidationResult validationResult = new EqualToConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        // TODO: This error message is not helpful or correct
        assertThat(validationResult.errors, hasItem("Value 1.1 must be a string | Field: boolean | Constraint: equalTo"));
    }

    // TODO: validation should fail, but currently succeeds
    @Disabled
    @Test
    public void validateValueType_withBooleanTypeAndStringValue_fails()
    {
        // Arrange
        EqualToConstraintDTO dto = atomicConstraintDTO("boolean").buildEqualTo("text");

        // Act
        ValidationResult validationResult = new EqualToConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Value 'text' must be a boolean | Field: boolean | Constraint: equalTo"));
    }
}
