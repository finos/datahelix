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
import com.scottlogic.datahelix.generator.profile.dtos.constraints.relations.EqualToFieldConstraintDTO;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.scottlogic.datahelix.generator.profile.creation.RelationsConstraintDTOBuilder.relationsConstraintDTO;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class EqualToFieldConstraintValidatorTests
{
    private final List<FieldDTO> fields = Arrays.asList
        (
            FieldDTOBuilder.fieldDTO("datetime", StandardSpecificFieldType.DATETIME).build(),
            FieldDTOBuilder.fieldDTO("datetimeOther", StandardSpecificFieldType.DATETIME).build(),
            FieldDTOBuilder.fieldDTO("decimal", StandardSpecificFieldType.DECIMAL).build(),
            FieldDTOBuilder.fieldDTO("integer", StandardSpecificFieldType.INTEGER).build(),
            FieldDTOBuilder.fieldDTO("text", StandardSpecificFieldType.STRING).build()
        );

    @Test
    public void validateEqualToFieldConstraint_withValidField_succeeds()
    {
        EqualToFieldConstraintDTO dto = relationsConstraintDTO("integer").equalToField("decimal");

        // Act
        ValidationResult validationResult = new EqualToFieldConstraintValidator(fields).validate(dto);

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateEqualToFieldConstraint_withValidOffsetField_succeeds()
    {
        EqualToFieldConstraintDTO dto = relationsConstraintDTO("datetime")
            .withOffset(3, "days")
            .equalToField("datetimeOther");

        // Act
        ValidationResult validationResult = new EqualToFieldConstraintValidator(fields).validate(dto);

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateEqualToFieldConstraint_withNullField_fails()
    {
        EqualToFieldConstraintDTO dto = relationsConstraintDTO(null).equalToField("decimal");

        // Act
        ValidationResult validationResult = new EqualToFieldConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Field must be specified | Constraint: 'equalToField'"));
    }

    @Test
    public void validateEqualToFieldConstraint_withEmptyField_fails()
    {
        EqualToFieldConstraintDTO dto = relationsConstraintDTO("").equalToField("decimal");

        // Act
        ValidationResult validationResult = new EqualToFieldConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Field must be specified | Constraint: 'equalToField'"));
    }

    @Test
    public void validateEqualToFieldConstraint_withUndefinedField_fails()
    {
        EqualToFieldConstraintDTO dto = relationsConstraintDTO("unknown").equalToField("decimal");

        // Act
        ValidationResult validationResult = new EqualToFieldConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("'unknown' must be defined in fields | Constraint: 'equalToField'"));
    }

    @Test
    public void validateEqualToFieldConstraint_withNullOtherField_fails()
    {
        EqualToFieldConstraintDTO dto = relationsConstraintDTO("integer").equalToField(null);

        // Act
        ValidationResult validationResult = new EqualToFieldConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Related field must be specified | Constraint: 'equalToField'"));
    }

    @Test
    public void validateEqualToFieldConstraint_withEmptyOtherField_fails()
    {
        EqualToFieldConstraintDTO dto = relationsConstraintDTO("integer").equalToField("");

        // Act
        ValidationResult validationResult = new EqualToFieldConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Related field must be specified | Constraint: 'equalToField'"));
    }

    @Test
    public void validateEqualToFieldConstraint_withUndefinedOtherField_fails()
    {
        EqualToFieldConstraintDTO dto = relationsConstraintDTO("integer").equalToField("unknown");

        // Act
        ValidationResult validationResult = new EqualToFieldConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("'unknown' must be defined in fields | Constraint: 'equalToField'"));
    }

    @Test
    public void validateEqualToFieldConstraint_withGranularityUnitOnUnsupportedFieldType_fails()
    {
        EqualToFieldConstraintDTO dto = relationsConstraintDTO("text")
            .withOffset(3, "days")
            .equalToField("text");

        // Act
        ValidationResult validationResult = new EqualToFieldConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Offset is not supported for string fields | Constraint: 'equalToField'"));
    }

    @Test
    public void validateEqualToFieldConstraint_withFieldAndOtherFieldOfDifferingType_fails()
    {
        EqualToFieldConstraintDTO dto = relationsConstraintDTO("integer").equalToField("text");

        // Act
        ValidationResult validationResult = new EqualToFieldConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Field type 'integer' doesn't match related field type 'text' | Constraint: 'equalToField'"));
    }
}
