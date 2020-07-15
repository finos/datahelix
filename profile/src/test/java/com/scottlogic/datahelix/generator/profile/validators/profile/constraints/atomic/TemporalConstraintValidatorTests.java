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
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.temporal.AfterConstraintDTO;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.scottlogic.datahelix.generator.profile.creation.AtomicConstraintDTOBuilder.atomicConstraintDTO;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TemporalConstraintValidatorTests
{

    private final List<FieldDTO> fields = Arrays.asList
        (
            FieldDTOBuilder.fieldDTO("datetime", StandardSpecificFieldType.DATETIME).build(),
            FieldDTOBuilder.fieldDTO("time", StandardSpecificFieldType.TIME).build()
        );

    @Test
    public void validateTemporalConstraint_withValidDatetime_succeeds()
    {
        // Arrange
        AfterConstraintDTO dto = atomicConstraintDTO("datetime").buildAfter("0001-01-01T00:00:00.000Z");

        // Act
        ValidationResult validationResult = new TemporalConstraintValidator(fields).validate(dto);

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateTemporalConstraint_withValidTime_succeeds()
    {
        // Arrange
        AfterConstraintDTO dto = atomicConstraintDTO("time").buildAfter("00:00:00");

        // Act
        ValidationResult validationResult = new TemporalConstraintValidator(fields).validate(dto);

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateTemporalConstraint_withNullField_fails()
    {
        // Arrange
        AfterConstraintDTO dto = atomicConstraintDTO(null).buildAfter("0001-01-01T00:00:00.000Z");

        // Act
        ValidationResult validationResult = new TemporalConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Field must be specified | Field: null | Constraint: after"));
    }

    @Test
    public void validateTemporalConstraint_withEmptyField_fails()
    {
        // Arrange
        AfterConstraintDTO dto = atomicConstraintDTO("").buildAfter("0001-01-01T00:00:00.000Z");

        // Act
        ValidationResult validationResult = new TemporalConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Field must be specified | Field:  | Constraint: after"));
    }

    @Test
    public void validateTemporalConstraint_withUndefinedField_fails()
    {
        // Arrange
        AfterConstraintDTO dto = atomicConstraintDTO("unknown").buildAfter("0001-01-01T00:00:00.000Z");

        // Act
        ValidationResult validationResult = new TemporalConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("unknown must be defined in fields | Field: unknown | Constraint: after"));
    }

    @Test
    public void validateTemporalConstraint_withUnspecifiedDatetime_fails()
    {
        // Arrange
        AfterConstraintDTO dto = atomicConstraintDTO("datetime").buildAfter("");

        // Act
        ValidationResult validationResult = new TemporalConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("DateTime must be specified | Field: datetime | Constraint: after"));
    }

    @Test
    public void validateTemporalConstraint_withNullDatetime_fails()
    {
        // Arrange
        AfterConstraintDTO dto = atomicConstraintDTO("datetime").buildAfter(null);

        // Act
        ValidationResult validationResult = new TemporalConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("DateTime must be specified | Field: datetime | Constraint: after"));
    }

    @Test
    public void validateTemporalConstraint_withUnspecifiedTime_fails()
    {
        // Arrange
        AfterConstraintDTO dto = atomicConstraintDTO("time").buildAfter("");

        // Act
        ValidationResult validationResult = new TemporalConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Time must be specified | Field: time | Constraint: after"));
    }

    @Test
    public void validateTemporalConstraint_withNullTime_fails()
    {
        // Arrange
        AfterConstraintDTO dto = atomicConstraintDTO("time").buildAfter(null);

        // Act
        ValidationResult validationResult = new TemporalConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Time must be specified | Field: time | Constraint: after"));
    }

    @Test
    public void validateTemporalConstraint_withInvalidDatetime_fails()
    {
        // Arrange
        AfterConstraintDTO dto = atomicConstraintDTO("datetime").buildAfter("invalid datetime");

        // Act
        ValidationResult validationResult = new TemporalConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Date string 'invalid datetime' must be in ISO-8601 format: Either yyyy-MM-ddTHH:mm:ss.SSS[Z] between 0001-01-01T00:00:00.000Z and 9999-12-31T23:59:59.999Z or yyyy-mm-dd between 0001-01-01 and 9999-12-31 | Field: datetime | Constraint: after"));
    }

    @Test
    public void validateTemporalConstraint_withInvalidTime_fails()
    {
        // Arrange
        AfterConstraintDTO dto = atomicConstraintDTO("time").buildAfter("invalid time");

        // Act
        ValidationResult validationResult = new TemporalConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Time string invalid time must be in ISO-8601 format: Either hh:mm:ss or hh:mm:ss.ms | Field: time | Constraint: after"));
    }

    @Test
    public void validateTemporalConstraint_withDatetimeBeforeMin_fails()
    {
        // Arrange
        AfterConstraintDTO dto = atomicConstraintDTO("datetime").buildAfter("0000-01-01T00:00:00.000Z");

        // Act
        ValidationResult validationResult = new TemporalConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Dates must be between 0001-01-01T00:00:00.000Z and 9999-12-31T23:59:59.999Z | Field: datetime | Constraint: after"));
    }

    @Test
    public void validateTemporalConstraint_withDatetimeAfterMax_fails()
    {
        // Arrange
        AfterConstraintDTO dto = atomicConstraintDTO("datetime").buildAfter("10000-01-01T00:00:00.000Z");

        // Act
        ValidationResult validationResult = new TemporalConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Dates must be between 0001-01-01T00:00:00.000Z and 9999-12-31T23:59:59.999Z | Field: datetime | Constraint: after"));
    }
}
