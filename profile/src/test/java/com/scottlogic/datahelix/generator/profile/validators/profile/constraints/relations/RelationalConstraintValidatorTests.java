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
import com.scottlogic.datahelix.generator.profile.dtos.constraints.relations.GreaterThanFieldConstraintDTO;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.scottlogic.datahelix.generator.profile.creation.RelationsConstraintDTOBuilder.relationsConstraintDTO;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class RelationalConstraintValidatorTests
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
    public void validateRelationalConstraint_withValidField_succeeds()
    {
        GreaterThanFieldConstraintDTO dto = relationsConstraintDTO("integer").greaterThanField("decimal");

        // Act
        ValidationResult validationResult = new RelationalConstraintValidator(fields).validate(dto);

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateRelationalConstraint_withValidOffsetedField_succeeds()
    {
        AfterFieldConstraintDTO dto = relationsConstraintDTO("datetime")
            .withOffset(3, "days")
            .afterField("datetimeOther");

        // Act
        ValidationResult validationResult = new RelationalConstraintValidator(fields).validate(dto);

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateRelationalConstraint_withNullField_fails()
    {
        GreaterThanFieldConstraintDTO dto = relationsConstraintDTO(null).greaterThanField("decimal");

        // Act
        ValidationResult validationResult = new RelationalConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Field must be specified | Constraint: 'greaterThanField'"));
    }

    @Test
    public void validateRelationalConstraint_withEmptyField_fails()
    {
        GreaterThanFieldConstraintDTO dto = relationsConstraintDTO("").greaterThanField("decimal");

        // Act
        ValidationResult validationResult = new RelationalConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Field must be specified | Constraint: 'greaterThanField'"));
    }

    @Test
    public void validateRelationalConstraint_withUndefinedField_fails()
    {
        GreaterThanFieldConstraintDTO dto = relationsConstraintDTO("unknown").greaterThanField("decimal");

        // Act
        ValidationResult validationResult = new RelationalConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("'unknown' must be defined in fields | Constraint: 'greaterThanField'"));
    }

    @Test
    public void validateRelationalConstraint_withNullOtherField_fails()
    {
        GreaterThanFieldConstraintDTO dto = relationsConstraintDTO("integer").greaterThanField(null);

        // Act
        ValidationResult validationResult = new RelationalConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Related field must be specified | Constraint: 'greaterThanField'"));
    }

    @Test
    public void validateRelationalConstraint_withEmptyOtherField_fails()
    {
        GreaterThanFieldConstraintDTO dto = relationsConstraintDTO("integer").greaterThanField("");

        // Act
        ValidationResult validationResult = new RelationalConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Related field must be specified | Constraint: 'greaterThanField'"));
    }

    @Test
    public void validateRelationalConstraint_withUndefinedOtherField_fails()
    {
        GreaterThanFieldConstraintDTO dto = relationsConstraintDTO("integer").greaterThanField("unknown");

        // Act
        ValidationResult validationResult = new RelationalConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("'unknown' must be defined in fields | Constraint: 'greaterThanField'"));
    }

    @Test
    public void validateRelationalConstraint_withFieldAndOtherFieldOfDifferingType_fails()
    {
        GreaterThanFieldConstraintDTO dto = relationsConstraintDTO("integer").greaterThanField("text");

        // Act
        ValidationResult validationResult = new RelationalConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Field type 'integer' doesn't match related field type 'text' | Constraint: 'greaterThanField'"));
    }

    @Test
    public void validateRelationalConstraint_withGranularityUnitOnUnsupportedFieldType_fails()
    {
        AfterFieldConstraintDTO dto = relationsConstraintDTO("text")
            .withOffset(3, "days")
            .afterField("text");

        // Act
        ValidationResult validationResult = new RelationalConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Granularity 'days' is not supported for string fields | Constraint: 'afterField'"));
    }

    // TODO: Validator should check for offset, even when no offset unit is present
    @Disabled
    @Test
    public void validateRelationalConstraint_withGranularityOnUnsupportedFieldType_fails()
    {
        AfterFieldConstraintDTO dto = relationsConstraintDTO("text").withOffset(3).afterField("text");

        // Act
        ValidationResult validationResult = new RelationalConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Granularity 'days' is not supported for string fields | Constraint: 'afterField'"));
    }

    // TODO: Add these checks to the validator
    @Disabled
    @Test
    public void validateRelationalConstraint_withInvalidFieldTypeForNumericFieldConstraint_fails()
    {
        GreaterThanFieldConstraintDTO dto = relationsConstraintDTO("datetime").greaterThanField("datetimeOther");

        // Act
        ValidationResult validationResult = new RelationalConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Field type 'datetime' doesn't match related constraint type 'numeric' | Constraint: 'greaterThanField'"));
    }

    @Disabled
    @Test
    public void validateRelationalConstraint_withInvalidFieldTypeForDateFieldConstraint_fails()
    {
        AfterFieldConstraintDTO dto = relationsConstraintDTO("integer").afterField("decimal");

        // Act
        ValidationResult validationResult = new RelationalConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Field type 'integer' doesn't match related constraint type 'datetime' | Constraint: 'afterField'"));
    }
}
