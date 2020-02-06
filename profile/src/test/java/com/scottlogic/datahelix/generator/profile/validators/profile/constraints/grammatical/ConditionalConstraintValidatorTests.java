/*
 * Copyright 2020 Scott Logic Ltd
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
import com.scottlogic.datahelix.generator.profile.creation.FieldDTOBuilder;
import com.scottlogic.datahelix.generator.profile.dtos.FieldDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.EqualToConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.grammatical.ConditionalConstraintDTO;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConditionalConstraintValidatorTests {

    private final List<FieldDTO> fields = Arrays.asList
        (
            FieldDTOBuilder.create("integer", StandardSpecificFieldType.INTEGER.toSpecificFieldType())
        );

    @Test
    public void validateConditionalConstraint_withNullThenConstraint_fails()
    {
        // Arrange
        EqualToConstraintDTO equalToConstraint = new EqualToConstraintDTO();
        equalToConstraint.field = "integer";
        equalToConstraint.value = 1;

        ConditionalConstraintDTO dto = new ConditionalConstraintDTO();
        dto.ifConstraint = equalToConstraint;
        dto.thenConstraint = null;
        dto.elseConstraint  = null;

        // Act
        ValidationResult validationResult = new ConditionalConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertEquals(1, validationResult.errors.size());
        assertTrue(validationResult.errors.contains("'if' constraint must also have an associated 'then' constraint"));
    }
}
