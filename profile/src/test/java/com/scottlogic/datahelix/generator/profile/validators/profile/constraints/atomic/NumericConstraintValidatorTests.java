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
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.numeric.LessThanConstraintDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static java.lang.Float.NaN;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NumericConstraintValidatorTests {

    private final List<FieldDTO> fields = Arrays.asList
        (
            FieldDTOBuilder.create("integer", StandardSpecificFieldType.INTEGER.toSpecificFieldType())
        );

    @Test
    public void validateNumericConstraint_withValidData_succeeds()
    {
        // Arrange
        LessThanConstraintDTO dto = new LessThanConstraintDTO();
        dto.field = "integer";
        dto.value = 1;

        // Act
        ValidationResult validationResult = new NumericConstraintValidator(fields, FieldType.NUMERIC).validate(dto);

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateNumericConstraint_withNullField_fails()
    {
        // Arrange
        LessThanConstraintDTO dto = new LessThanConstraintDTO();
        dto.field = null;
        dto.value = 1;

        // Act
        ValidationResult validationResult = new NumericConstraintValidator(fields, FieldType.NUMERIC).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateNumericConstraint_withEmptyField_fails()
    {
        // Arrange
        LessThanConstraintDTO dto = new LessThanConstraintDTO();
        dto.field = "";
        dto.value = 1;

        // Act
        ValidationResult validationResult = new NumericConstraintValidator(fields, FieldType.NUMERIC).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateNumericConstraint_withUndefinedField_fails()
    {
        // Arrange
        LessThanConstraintDTO dto = new LessThanConstraintDTO();
        dto.field = "unknown";
        dto.value = 1;

        // Act
        ValidationResult validationResult = new NumericConstraintValidator(fields, FieldType.NUMERIC).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateNumericConstraint_withNullValue_fails() {
        // Arrange
        LessThanConstraintDTO dto = new LessThanConstraintDTO();
        dto.field = "integer";
        dto.value = null;

        // Act
        ValidationResult validationResult = new NumericConstraintValidator(fields, FieldType.NUMERIC).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateNumericConstraint_withValueLessThanMin_fails() {
        // Arrange
        LessThanConstraintDTO dto = new LessThanConstraintDTO();
        dto.field = "integer";
        dto.value = Defaults.NUMERIC_MIN.subtract(new BigDecimal(1));

        // Act
        ValidationResult validationResult = new NumericConstraintValidator(fields, FieldType.NUMERIC).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateNumericConstraint_withValueGreaterThanMax_fails() {
        // Arrange
        LessThanConstraintDTO dto = new LessThanConstraintDTO();
        dto.field = "integer";
        dto.value = Defaults.NUMERIC_MAX.add(new BigDecimal(1));

        // Act
        ValidationResult validationResult = new NumericConstraintValidator(fields, FieldType.NUMERIC).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }
}
