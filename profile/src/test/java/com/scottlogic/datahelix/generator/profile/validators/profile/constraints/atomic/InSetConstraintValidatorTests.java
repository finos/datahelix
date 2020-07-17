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
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.InSetConstraintDTO;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.scottlogic.datahelix.generator.profile.creation.AtomicConstraintDTOBuilder.atomicConstraintDTO;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class InSetConstraintValidatorTests
{

    private final List<FieldDTO> fields = Arrays.asList
        (
            FieldDTOBuilder.fieldDTO("text", StandardSpecificFieldType.STRING).build()
        );
    private final List<Object> stringValues = Arrays.asList
        (
            "value",
            "value"
        );
    private final List<Object> valuesOfAllTypes = Arrays.asList
        (
            "value",
            1.1,
            true
        );

    @Test
    public void validateInSetConstraint_withValidField_succeeds()
    {
        // Arrange
        InSetConstraintDTO dto = atomicConstraintDTO("text").buildInSet(stringValues);

        // Act
        ValidationResult validationResult = new InSetConstraintValidator(fields).validate(dto);

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateInSetConstraint_withNullField_fails()
    {
        // Arrange
        InSetConstraintDTO dto = atomicConstraintDTO(null).buildInSet(stringValues);

        // Act
        ValidationResult validationResult = new InSetConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Field must be specified | Field: NULL | Constraint: 'inSet'"));
    }

    @Test
    public void validateInSetConstraint_withEmptyField_fails()
    {
        // Arrange
        InSetConstraintDTO dto = atomicConstraintDTO("").buildInSet(stringValues);

        // Act
        ValidationResult validationResult = new InSetConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Field must be specified | Field: '' | Constraint: 'inSet'"));
    }

    @Test
    public void validateInSetConstraint_withUndefinedField_fails()
    {
        // Arrange
        InSetConstraintDTO dto = atomicConstraintDTO("unknown").buildInSet(stringValues);

        // Act
        ValidationResult validationResult = new InSetConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("'unknown' must be defined in fields | Field: 'unknown' | Constraint: 'inSet'"));
    }

    @Test
    public void validateInSetConstraint_withEmptyValue_fails()
    {
        // Arrange
        InSetConstraintDTO dto = atomicConstraintDTO("text").buildInSet(emptyList());

        // Act
        ValidationResult validationResult = new InSetConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("In set values must be specified | Field: 'text' | Constraint: 'inSet'"));
    }

    @Test
    public void validateInSetConstraint_withNullValue_fails()
    {
        // Arrange
        InSetConstraintDTO dto = atomicConstraintDTO("text").buildInSet(null);

        // Act
        ValidationResult validationResult = new InSetConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("In set values must be specified | Field: 'text' | Constraint: 'inSet'"));
    }

    @Test
    public void validateInSetConstraint_withInvalidData_fails()
    {
        // Arrange
        InSetConstraintDTO dto = atomicConstraintDTO("text").buildInSet(valuesOfAllTypes);

        // Act
        ValidationResult validationResult = new InSetConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(2));
        assertThat(validationResult.errors, hasItem("Value 1.1 must be a string | Field: 'text' | Constraint: 'inSet'"));
        assertThat(validationResult.errors, hasItem("Value true must be a string | Field: 'text' | Constraint: 'inSet'"));
    }
}
