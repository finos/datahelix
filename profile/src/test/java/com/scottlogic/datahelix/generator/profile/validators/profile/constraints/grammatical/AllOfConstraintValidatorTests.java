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
package com.scottlogic.datahelix.generator.profile.validators.profile.constraints.grammatical;

import com.scottlogic.datahelix.generator.common.profile.StandardSpecificFieldType;
import com.scottlogic.datahelix.generator.common.validators.ValidationResult;
import com.scottlogic.datahelix.generator.profile.creation.ConstraintDTOBuilder;
import com.scottlogic.datahelix.generator.profile.creation.FieldDTOBuilder;
import com.scottlogic.datahelix.generator.profile.dtos.FieldDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.grammatical.AllOfConstraintDTO;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.scottlogic.datahelix.generator.profile.creation.AtomicConstraintDTOBuilder.atomicConstraintDTO;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class AllOfConstraintValidatorTests
{

    private final List<FieldDTO> fields = Arrays.asList
        (
            FieldDTOBuilder.fieldDTO("text", StandardSpecificFieldType.STRING).build(),
            FieldDTOBuilder.fieldDTO("decimal", StandardSpecificFieldType.DECIMAL).build(),
            FieldDTOBuilder.fieldDTO("boolean", StandardSpecificFieldType.BOOLEAN).build()
        );

    @Test
    public void validateAllOfConstraint_withSubConstraints_succeeds()
    {
        AllOfConstraintDTO dto = ConstraintDTOBuilder.allOf(
            atomicConstraintDTO("text").buildIsNull(false),
            atomicConstraintDTO("decimal").buildIsNull(false)
        );

        // Act
        ValidationResult validationResult = new AllOfConstraintValidator(fields).validate(dto);

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateAllOfConstraint_withUnspecifiedSubConstraints_fails()
    {
        AllOfConstraintDTO dto = ConstraintDTOBuilder.allOfValue(null);

        // Act
        ValidationResult validationResult = new AllOfConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Sub constraints must be specified | Constraint: allOf"));
    }

    @Test
    public void validateAllOfConstraint_withEmptySubConstraints_fails()
    {
        AllOfConstraintDTO dto = ConstraintDTOBuilder.allOf();

        // Act
        ValidationResult validationResult = new AllOfConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Sub constraints must be specified | Constraint: allOf"));
    }

    @Test
    public void validateAllOfConstraint_withNullSubConstraint_fails()
    {
        AllOfConstraintDTO dto = ConstraintDTOBuilder.allOf(atomicConstraintDTO("text").buildIsNull(false), null);

        // Act
        ValidationResult validationResult = new AllOfConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Constraint must not be null"));
    }

    @Test
    public void validateAllOfConstraint_withInvalidConstraint_fails()
    {
        AllOfConstraintDTO dto = ConstraintDTOBuilder.allOf(atomicConstraintDTO("unknown").buildIsNull(false));

        // Act
        ValidationResult validationResult = new AllOfConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("unknown must be defined in fields | Field: unknown | Constraint: isNull"));
    }
}
