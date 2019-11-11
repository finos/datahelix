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
package com.scottlogic.deg.profile.validators.profile;

import com.scottlogic.deg.common.profile.SpecificFieldType;
import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.profile.creation.ConstraintDTOBuilder;
import com.scottlogic.deg.profile.creation.FieldDTOBuilder;
import com.scottlogic.deg.profile.dtos.FieldDTO;
import com.scottlogic.deg.profile.dtos.RuleDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RuleValidatorTests
{
    private final List<FieldDTO> fields = Arrays.asList
        (
            FieldDTOBuilder.create("text", SpecificFieldType.STRING),
            FieldDTOBuilder.create("integer", SpecificFieldType.INTEGER)
        );
    private final RuleValidator ruleValidator = new RuleValidator(fields);

    @Test
    public void validateRule_withValidData_succeeds()
    {
        // Arrange
        RuleDTO dto = new RuleDTO();
        dto.constraints = Collections.singletonList(ConstraintDTOBuilder.create("integer", 1));

        // Act
        ValidationResult validationResult = ruleValidator.validate(dto);

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateRule_withNullConstraints_fails()
    {
        // Arrange
        RuleDTO dto = new RuleDTO();

        // Act
        ValidationResult validationResult = ruleValidator.validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateRule_withEmptyConstraints_fails()
    {
        // Arrange
        RuleDTO dto = new RuleDTO();
        dto.constraints = new ArrayList<>();

        // Act
        ValidationResult validationResult = ruleValidator.validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }
}