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
package com.scottlogic.datahelix.generator.profile.validators.profile;

import com.scottlogic.datahelix.generator.common.validators.ValidationResult;
import com.scottlogic.datahelix.generator.core.builders.GenerationConfigSourceBuilder;
import com.scottlogic.datahelix.generator.core.config.detail.CombinationStrategyType;
import com.scottlogic.datahelix.generator.core.generation.GenerationConfigSource;
import com.scottlogic.datahelix.generator.profile.dtos.ProfileDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.grammatical.ConditionalConstraintDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static com.scottlogic.datahelix.generator.profile.creation.AtomicConstraintDTOBuilder.atomicConstraintDTO;
import static com.scottlogic.datahelix.generator.profile.creation.FieldDTOBuilder.fieldDTOWithStringType;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ProfileValidatorTests
{
    private final GenerationConfigSourceBuilder configSourceBuilder = new GenerationConfigSourceBuilder();

    @Test
    public void validateProfile_withValidData_succeeds()
    {
        // Arrange
        GenerationConfigSource configSource = configSourceBuilder.build();
        ProfileValidator profileValidator = new ProfileValidator(configSource);
        ProfileDTO dto = new ProfileDTO();
        dto.fields = Collections.singletonList(fieldDTOWithStringType("test").build());
        dto.constraints = new ArrayList<>();

        // Act
        ValidationResult validationResult = profileValidator.validate(dto);

        // Assert
        assertTrue(validationResult.isSuccess);
        assertThat(validationResult.errors, emptyIterable());
    }

    @Test
    public void validateProfile_withNullFieldsAndNullConstraints_fails()
    {
        // Arrange
        GenerationConfigSource configSource = configSourceBuilder.build();
        ProfileValidator profileValidator = new ProfileValidator(configSource);
        ProfileDTO dto = new ProfileDTO();
        dto.fields = null;
        dto.constraints = null;

        // Act
        ValidationResult validationResult = profileValidator.validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Fields must be specified"));
//        TODO: Should also return this aswell
//        assertThat(validationResult.errors, hasItem("Constraints must be specified"));
    }

    @Test
    public void validateProfile_withNullFields_fails()
    {
        // Arrange
        GenerationConfigSource configSource = configSourceBuilder.build();
        ProfileValidator profileValidator = new ProfileValidator(configSource);
        ProfileDTO dto = new ProfileDTO();
        dto.fields = null;
        dto.constraints = new ArrayList<>();

        // Act
        ValidationResult validationResult = profileValidator.validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Fields must be specified"));
    }

    @Test
    public void validateProfile_withNullConstraints_fails()
    {
        // Arrange
        GenerationConfigSource configSource = configSourceBuilder.build();
        ProfileValidator profileValidator = new ProfileValidator(configSource);
        ProfileDTO dto = new ProfileDTO();
        dto.fields = Collections.singletonList(fieldDTOWithStringType("test").build());
        dto.constraints = null;

        // Act
        ValidationResult validationResult = profileValidator.validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Constraints must be specified"));
    }

    @Test
    public void validateProfile_withEmptyFields_fails()
    {
        // Arrange
        GenerationConfigSource configSource = configSourceBuilder.build();
        ProfileValidator profileValidator = new ProfileValidator(configSource);
        ProfileDTO dto = new ProfileDTO();
        dto.fields = new ArrayList<>();
        dto.constraints = new ArrayList<>();

        // Act
        ValidationResult validationResult = profileValidator.validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Fields must be specified"));
    }

    @Test
    public void validateProfile_withNonUniqueFields_fails()
    {
        // Arrange
        GenerationConfigSource configSource = configSourceBuilder.build();
        ProfileValidator profileValidator = new ProfileValidator(configSource);
        ProfileDTO dto = new ProfileDTO();
        dto.fields = Arrays.asList(fieldDTOWithStringType("test").build(), fieldDTOWithStringType("test").build());
        dto.constraints = new ArrayList<>();

        // Act
        ValidationResult validationResult = profileValidator.validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Field names must be unique | Duplicates: 'test'"));
    }

    @Test
    public void validateProfile_withUniqueFieldsInNonMinimalCombinationStrategy_fails()
    {
        // Arrange
        GenerationConfigSource configSource = configSourceBuilder
            .withCombinationStrategyType(CombinationStrategyType.EXHAUSTIVE)
            .build();
        ProfileValidator profileValidator = new ProfileValidator(configSource);
        ProfileDTO dto = new ProfileDTO();
        dto.fields = Collections.singletonList(fieldDTOWithStringType("test").withUniqueness().build());
        dto.constraints = new ArrayList<>();

        // Act
        ValidationResult validationResult = profileValidator.validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Unique fields do not work when not using Minimal combination strategy"));
    }

    @Test
    public void validateProfile_withUniqueFieldReferencedInIf_fails()
    {
        // Arrange
        GenerationConfigSource configSource = configSourceBuilder.build();
        ProfileValidator profileValidator = new ProfileValidator(configSource);
        ProfileDTO dto = new ProfileDTO();
        dto.fields = Arrays.asList(
            fieldDTOWithStringType("test").build(),
            fieldDTOWithStringType("uniq").withUniqueness().build());

        ConditionalConstraintDTO ifDto = new ConditionalConstraintDTO();
        ifDto.ifConstraint = atomicConstraintDTO("uniq").buildOfLength(8);
        ifDto.thenConstraint = atomicConstraintDTO("test").buildEqualTo("val");
        ifDto.elseConstraint = atomicConstraintDTO("test").buildEqualTo("other");
        dto.constraints = Collections.singletonList(ifDto);

        // Act
        ValidationResult validationResult = profileValidator.validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
        assertThat(validationResult.errors, iterableWithSize(1));
        assertThat(validationResult.errors, hasItem("Unique field 'uniq' cannot be referenced in IF statement"));
    }
}