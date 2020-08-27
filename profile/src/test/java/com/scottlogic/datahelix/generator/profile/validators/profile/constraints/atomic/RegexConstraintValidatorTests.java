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
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.textual.ContainsRegexConstraintDTO;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.scottlogic.datahelix.generator.profile.creation.AtomicConstraintDTOBuilder.atomicConstraintDTO;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class RegexConstraintValidatorTests
{
    private final static String newLine = System.lineSeparator();


    private final List<FieldDTO> fields = Arrays.asList
        (
            FieldDTOBuilder.fieldDTO("text", StandardSpecificFieldType.STRING).build(),
            FieldDTOBuilder.fieldDTO("decimal", StandardSpecificFieldType.DECIMAL).build()
        );

    @Test
    public void validateRegexConstraint_withValidRegex_succeeds()
    {
        // Arrange
        ContainsRegexConstraintDTO dto = atomicConstraintDTO("text").buildContainsRegex("/a{0,3}/");

        // Act
        ValidationResult validationResult = new RegexConstraintValidator(fields).validate(dto);

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateRegexConstraint_withNullField_fails()
    {
        // Arrange
        ContainsRegexConstraintDTO dto = atomicConstraintDTO(null).buildContainsRegex("/a{0,3}/");

        // Act
        ValidationResult validationResult = new RegexConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Field must be specified | Field: NULL | Constraint: 'containingRegex'"));
    }

    @Test
    public void validateRegexConstraint_withEmptyField_fails()
    {
        // Arrange
        ContainsRegexConstraintDTO dto = atomicConstraintDTO("").buildContainsRegex("/a{0,3}/");

        // Act
        ValidationResult validationResult = new RegexConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Field must be specified | Field: '' | Constraint: 'containingRegex'"));
    }

    @Test
    public void validateRegexConstraint_withUndefinedField_fails()
    {
        // Arrange
        ContainsRegexConstraintDTO dto = atomicConstraintDTO("unknown").buildContainsRegex("/a{0,3}/");

        // Act
        ValidationResult validationResult = new RegexConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("'unknown' must be defined in fields | Field: 'unknown' | Constraint: 'containingRegex'"));
    }

    @Test
    public void validateRegexConstraint_withNullRegex_fails()
    {
        // Arrange
        ContainsRegexConstraintDTO dto = atomicConstraintDTO("text").buildContainsRegex(null);

        // Act
        ValidationResult validationResult = new RegexConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Text must be specified | Field: 'text' | Constraint: 'containingRegex'"));
    }

    @Test
    public void validateRegexConstraint_withInvalidRegex_fails()
    {
        // Arrange
        ContainsRegexConstraintDTO dto = atomicConstraintDTO("text").buildContainsRegex("/*****/");

        // Act
        ValidationResult validationResult = new RegexConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem(String.format("Regex is invalid | Dangling meta character '*' near index 2%s/*****/%s  ^", newLine, newLine)));
    }

    @Test
    public void validateRegexConstraint_withInvalidFieldType_fails()
    {
        // Arrange
        ContainsRegexConstraintDTO dto = atomicConstraintDTO("decimal").buildContainsRegex("/a{0,3}/");

        // Act
        ValidationResult validationResult = new RegexConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Expected field type STRING doesn't match field type NUMERIC | Field: 'decimal' | Constraint: 'containingRegex'"));
    }
}
