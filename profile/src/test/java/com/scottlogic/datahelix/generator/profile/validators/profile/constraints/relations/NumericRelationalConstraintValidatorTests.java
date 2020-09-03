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
import com.scottlogic.datahelix.generator.profile.dtos.constraints.relations.GreaterThanFieldConstraintDTO;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.scottlogic.datahelix.generator.profile.creation.RelationsConstraintDTOBuilder.relationsConstraintDTO;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class NumericRelationalConstraintValidatorTests
{
    private final List<FieldDTO> fields = Arrays.asList
        (
            FieldDTOBuilder.fieldDTO("decimal", StandardSpecificFieldType.DECIMAL).build(),
            FieldDTOBuilder.fieldDTO("integer", StandardSpecificFieldType.INTEGER).build(),
            FieldDTOBuilder.fieldDTO("text", StandardSpecificFieldType.STRING).build()
        );

    @Test
    public void validateNumericRelationalConstraint_withValidField_succeeds()
    {
        GreaterThanFieldConstraintDTO dto = relationsConstraintDTO("integer").greaterThanField("decimal");

        // Act
        ValidationResult validationResult = new NumericRelationalConstraintValidator<GreaterThanFieldConstraintDTO>(fields).validate(dto);

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateNumericRelationalConstraint_withValidOffsetField_succeeds()
    {
        GreaterThanFieldConstraintDTO dto = relationsConstraintDTO("integer")
            .withOffset(3)
            .greaterThanField("decimal");

        // Act
        ValidationResult validationResult = new NumericRelationalConstraintValidator<GreaterThanFieldConstraintDTO>(fields).validate(dto);

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateNumericRelationalConstraint_withNullField_fails()
    {
        GreaterThanFieldConstraintDTO dto = relationsConstraintDTO(null).greaterThanField("decimal");

        // Act
        ValidationResult validationResult = new NumericRelationalConstraintValidator<GreaterThanFieldConstraintDTO>(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Field must be specified | Constraint: 'greaterThanField'"));
    }

    @Test
    public void validateNumericRelationalConstraint_withEmptyField_fails()
    {
        GreaterThanFieldConstraintDTO dto = relationsConstraintDTO("").greaterThanField("decimal");

        // Act
        ValidationResult validationResult = new NumericRelationalConstraintValidator<GreaterThanFieldConstraintDTO>(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Field must be specified | Constraint: 'greaterThanField'"));
    }

    @Test
    public void validateNumericRelationalConstraint_withUndefinedField_fails()
    {
        GreaterThanFieldConstraintDTO dto = relationsConstraintDTO("unknown").greaterThanField("decimal");

        // Act
        ValidationResult validationResult = new NumericRelationalConstraintValidator<GreaterThanFieldConstraintDTO>(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("'unknown' must be defined in fields | Constraint: 'greaterThanField'"));
    }

    @Test
    public void validateNumericRelationalConstraint_withNonNumericField_fails()
    {
        GreaterThanFieldConstraintDTO dto = relationsConstraintDTO("text").greaterThanField("decimal");

        // Act
        ValidationResult validationResult = new NumericRelationalConstraintValidator<GreaterThanFieldConstraintDTO>(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Field must be numeric however it has type STRING | Constraint: 'greaterThanField'"));
    }

    @Test
    public void validateNumericRelationalConstraint_withNullOtherField_fails()
    {
        GreaterThanFieldConstraintDTO dto = relationsConstraintDTO("integer").greaterThanField(null);

        // Act
        ValidationResult validationResult = new NumericRelationalConstraintValidator<GreaterThanFieldConstraintDTO>(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Related field must be specified | Constraint: 'greaterThanField'"));
    }

    @Test
    public void validateNumericRelationalConstraint_withEmptyOtherField_fails()
    {
        GreaterThanFieldConstraintDTO dto = relationsConstraintDTO("integer").greaterThanField("");

        // Act
        ValidationResult validationResult = new NumericRelationalConstraintValidator<GreaterThanFieldConstraintDTO>(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Related field must be specified | Constraint: 'greaterThanField'"));
    }

    @Test
    public void validateNumericRelationalConstraint_withUndefinedOtherField_fails()
    {
        GreaterThanFieldConstraintDTO dto = relationsConstraintDTO("integer").greaterThanField("unknown");

        // Act
        ValidationResult validationResult = new NumericRelationalConstraintValidator<GreaterThanFieldConstraintDTO>(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("'unknown' must be defined in fields | Constraint: 'greaterThanField'"));
    }

    @Test
    public void validateNumericRelationalConstraint_withNonNumericOtherField_fails()
    {
        GreaterThanFieldConstraintDTO dto = relationsConstraintDTO("integer").greaterThanField("text");

        // Act
        ValidationResult validationResult = new NumericRelationalConstraintValidator<GreaterThanFieldConstraintDTO>(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Related field must be numeric however it has type STRING | Constraint: 'greaterThanField'"));
    }

    @Test
    public void validateNumericRelationalConstraint_withInvalidGranularityUnit_fails()
    {
        GreaterThanFieldConstraintDTO dto = relationsConstraintDTO("integer")
            .withOffset(3, "days")
            .greaterThanField("decimal");

        // Act
        ValidationResult validationResult = new NumericRelationalConstraintValidator<GreaterThanFieldConstraintDTO>(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Can't interpret numeric granularity expression: 'days' | Constraint: 'greaterThanField'"));
    }
}
