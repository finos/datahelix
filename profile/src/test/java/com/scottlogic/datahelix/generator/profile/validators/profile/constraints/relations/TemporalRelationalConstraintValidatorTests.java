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
package com.scottlogic.datahelix.generator.profile.validators.profile.constraints.relations;

import com.scottlogic.datahelix.generator.common.profile.StandardSpecificFieldType;
import com.scottlogic.datahelix.generator.common.validators.ValidationResult;
import com.scottlogic.datahelix.generator.profile.creation.FieldDTOBuilder;
import com.scottlogic.datahelix.generator.profile.dtos.FieldDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.relations.AfterFieldConstraintDTO;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.scottlogic.datahelix.generator.profile.creation.RelationsConstraintDTOBuilder.relationsConstraintDTO;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TemporalRelationalConstraintValidatorTests
{
    private final List<FieldDTO> fields = Arrays.asList
        (
            FieldDTOBuilder.fieldDTO("datetime", StandardSpecificFieldType.DATETIME).build(),
            FieldDTOBuilder.fieldDTO("datetimeOther", StandardSpecificFieldType.DATETIME).build(),
            FieldDTOBuilder.fieldDTO("text", StandardSpecificFieldType.STRING).build()
        );

    @Test
    public void validateTemporalRelationalConstraint_withValidField_succeeds()
    {
        AfterFieldConstraintDTO dto = relationsConstraintDTO("datetime").afterField("datetimeOther");

        // Act
        ValidationResult validationResult = new TemporalRelationalConstraintValidator<AfterFieldConstraintDTO>(fields).validate(dto);

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateTemporalRelationalConstraint_withValidOffsetField_succeeds()
    {
        AfterFieldConstraintDTO dto = relationsConstraintDTO("datetime")
            .withOffset(3, "days")
            .afterField("datetimeOther");

        // Act
        ValidationResult validationResult = new TemporalRelationalConstraintValidator<AfterFieldConstraintDTO>(fields).validate(dto);

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateTemporalRelationalConstraint_withNullField_fails()
    {
        AfterFieldConstraintDTO dto = relationsConstraintDTO(null).afterField("datetimeOther");

        // Act
        ValidationResult validationResult = new TemporalRelationalConstraintValidator<AfterFieldConstraintDTO>(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Field must be specified | Constraint: 'afterField'"));
    }

    @Test
    public void validateTemporalRelationalConstraint_withEmptyField_fails()
    {
        AfterFieldConstraintDTO dto = relationsConstraintDTO("").afterField("datetimeOther");

        // Act
        ValidationResult validationResult = new TemporalRelationalConstraintValidator<AfterFieldConstraintDTO>(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Field must be specified | Constraint: 'afterField'"));
    }

    @Test
    public void validateTemporalRelationalConstraint_withUndefinedField_fails()
    {
        AfterFieldConstraintDTO dto = relationsConstraintDTO("unknown").afterField("datetimeOther");

        // Act
        ValidationResult validationResult = new TemporalRelationalConstraintValidator<AfterFieldConstraintDTO>(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("'unknown' must be defined in fields | Constraint: 'afterField'"));
    }

    @Test
    public void validateTemporalRelationalConstraint_withNonTemporalField_fails()
    {
        AfterFieldConstraintDTO dto = relationsConstraintDTO("text").afterField("datetimeOther");

        // Act
        ValidationResult validationResult = new TemporalRelationalConstraintValidator<AfterFieldConstraintDTO>(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Field must be time/datetime however it has type STRING | Constraint: 'afterField'"));
    }

    @Test
    public void validateTemporalRelationalConstraint_withNullOtherField_fails()
    {
        AfterFieldConstraintDTO dto = relationsConstraintDTO("datetime").afterField(null);

        // Act
        ValidationResult validationResult = new TemporalRelationalConstraintValidator<AfterFieldConstraintDTO>(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Related field must be specified | Constraint: 'afterField'"));
    }

    @Test
    public void validateTemporalRelationalConstraint_withEmptyOtherField_fails()
    {
        AfterFieldConstraintDTO dto = relationsConstraintDTO("datetime").afterField("");

        // Act
        ValidationResult validationResult = new TemporalRelationalConstraintValidator<AfterFieldConstraintDTO>(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Related field must be specified | Constraint: 'afterField'"));
    }

    @Test
    public void validateTemporalRelationalConstraint_withUndefinedOtherField_fails()
    {
        AfterFieldConstraintDTO dto = relationsConstraintDTO("datetime").afterField("unknown");

        // Act
        ValidationResult validationResult = new TemporalRelationalConstraintValidator<AfterFieldConstraintDTO>(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("'unknown' must be defined in fields | Constraint: 'afterField'"));
    }

    @Test
    public void validateTemporalRelationalConstraint_withNonTemporaltherField_fails()
    {
        AfterFieldConstraintDTO dto = relationsConstraintDTO("datetime").afterField("text");

        // Act
        ValidationResult validationResult = new TemporalRelationalConstraintValidator<AfterFieldConstraintDTO>(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Related field must be time/datetime however it has type STRING | Constraint: 'afterField'"));
    }

    @Test
    public void validateTemporalRelationalConstraint_withInvalidGranularityUnit_fails()
    {
        AfterFieldConstraintDTO dto = relationsConstraintDTO("datetime")
            .withOffset(3, "decade")
            .afterField("datetimeOther");

        // Act
        ValidationResult validationResult = new TemporalRelationalConstraintValidator<AfterFieldConstraintDTO>(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Granularity 'decade' is not supported | Constraint: 'afterField'"));
    }
}
