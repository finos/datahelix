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
import com.scottlogic.datahelix.generator.profile.dtos.constraints.relations.InMapConstraintDTO;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.scottlogic.datahelix.generator.profile.creation.RelationsConstraintDTOBuilder.relationsConstraintDTO;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class InMapConstraintValidatorTests
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
    public void validateInMapConstraint_withValidField_succeeds()
    {
        InMapConstraintDTO dto = relationsConstraintDTO("text").inMap(stringValues);

        // Act
        ValidationResult validationResult = new InMapConstraintValidator(fields).validate(dto);

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateInMapConstraint_withNullField_fails()
    {
        InMapConstraintDTO dto = relationsConstraintDTO(null).inMap(stringValues);

        // Act
        ValidationResult validationResult = new InMapConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Field must be specified | Constraint: inMap"));
    }

    @Test
    public void validateInMapConstraint_withEmptyField_fails()
    {
        InMapConstraintDTO dto = relationsConstraintDTO("").inMap(stringValues);

        // Act
        ValidationResult validationResult = new InMapConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Field must be specified | Constraint: inMap"));
    }

    @Test
    public void validateInMapConstraint_withUndefinedField_fails()
    {
        InMapConstraintDTO dto = relationsConstraintDTO("unknown").inMap(stringValues);

        // Act
        ValidationResult validationResult = new InMapConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("unknown must be defined in fields | Constraint: inMap"));
    }

    @Test
    public void validateInMapConstraint_withEmptyValue_fails()
    {
        InMapConstraintDTO dto = relationsConstraintDTO("text").inMap(emptyList());

        // Act
        ValidationResult validationResult = new InMapConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Values must be specified | Constraint: inMap"));
    }

    @Test
    public void validateInMapConstraint_withNullValue_fails()
    {
        InMapConstraintDTO dto = relationsConstraintDTO("text").inMap(null);

        // Act
        ValidationResult validationResult = new InMapConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Values must be specified | Constraint: inMap"));
    }

    // TODO: Type checking of value list is missing
    @Disabled
    @Test
    public void validateInMapConstraint_withInvalidData_fails()
    {
        InMapConstraintDTO dto = relationsConstraintDTO("text").inMap(valuesOfAllTypes);

        // Act
        ValidationResult validationResult = new InMapConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(2));
        assertThat(validationResult.errors, hasItem("Value 1.1 must be a string | Field: text | Constraint: inMap"));
        assertThat(validationResult.errors, hasItem("Value true must be a string | Field: text | Constraint: inMap"));
    }
}
