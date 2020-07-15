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
import com.scottlogic.datahelix.generator.profile.dtos.constraints.grammatical.AnyOfConstraintDTO;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.scottlogic.datahelix.generator.profile.creation.AtomicConstraintDTOBuilder.atomicConstraintDTO;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class AnyOfConstraintValidatorTests
{

    private final List<FieldDTO> fields = Arrays.asList
        (
            FieldDTOBuilder.fieldDTO("text", StandardSpecificFieldType.STRING).build(),
            FieldDTOBuilder.fieldDTO("decimal", StandardSpecificFieldType.DECIMAL).build(),
            FieldDTOBuilder.fieldDTO("boolean", StandardSpecificFieldType.BOOLEAN).build()
        );

    @Test
    public void validateAnyOfConstraint_withSubConstraints_succeeds()
    {
        AnyOfConstraintDTO dto = ConstraintDTOBuilder.anyOf(
            atomicConstraintDTO("text").buildIsNull(false),
            atomicConstraintDTO("decimal").buildIsNull(false)
        );

        // Act
        ValidationResult validationResult = new AnyOfConstraintValidator(fields).validate(dto);

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateAnyOfConstraint_withUnspecifiedSubConstraints_fails()
    {
        AnyOfConstraintDTO dto = ConstraintDTOBuilder.anyOfValue(null);

        // Act
        ValidationResult validationResult = new AnyOfConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Sub constraints must be specified | Constraint: 'anyOf'"));
    }

    @Test
    public void validateAnyOfConstraint_withEmptySubConstraints_fails()
    {
        AnyOfConstraintDTO dto = ConstraintDTOBuilder.anyOf();

        // Act
        ValidationResult validationResult = new AnyOfConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Sub constraints must be specified | Constraint: 'anyOf'"));
    }

    @Test
    public void validateAnyOfConstraint_withNullSubConstraint_fails()
    {
        AnyOfConstraintDTO dto = ConstraintDTOBuilder.anyOf(atomicConstraintDTO("text").buildIsNull(false), null);

        // Act
        ValidationResult validationResult = new AnyOfConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Constraint must not be null"));
    }

    @Test
    public void validateAnyOfConstraint_withInvalidConstraint_fails()
    {
        AnyOfConstraintDTO dto = ConstraintDTOBuilder.anyOf(atomicConstraintDTO("unknown").buildIsNull(false));

        // Act
        ValidationResult validationResult = new AnyOfConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("'unknown' must be defined in fields | Field: 'unknown' | Constraint: 'isNull'"));
    }
}
