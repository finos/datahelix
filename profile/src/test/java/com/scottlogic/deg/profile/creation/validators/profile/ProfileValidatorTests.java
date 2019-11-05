package com.scottlogic.deg.profile.creation.validators.profile;

import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.profile.creation.FieldDTOBuilder;
import com.scottlogic.deg.profile.creation.dtos.ProfileDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ProfileValidatorTests
{
    private final ProfileValidator profileValidator = new ProfileValidator();

    @Test
    public void validateProfile_withValidData_succeeds()
    {
        // Arrange
        ProfileDTO dto = new ProfileDTO();
        dto.fields = Collections.singletonList(FieldDTOBuilder.create("test"));
        dto.rules = new ArrayList<>();

        // Act
        ValidationResult validationResult = profileValidator.validate(dto);

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateProfile_withNullFields_fails()
    {
        // Arrange
        ProfileDTO dto = new ProfileDTO();
        dto.fields = null;
        dto.rules = new ArrayList<>();

        // Act
        ValidationResult validationResult = profileValidator.validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateProfile_withEmptyFields_fails()
    {
        // Arrange
        ProfileDTO dto = new ProfileDTO();
        dto.fields = new ArrayList<>();
        dto.rules = new ArrayList<>();

        // Act
        ValidationResult validationResult = profileValidator.validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateProfile_withNonUniqueFields_fails()
    {
        // Arrange
        ProfileDTO dto = new ProfileDTO();
        dto.fields = Arrays.asList(FieldDTOBuilder.create("test"), FieldDTOBuilder.create("test"));
        dto.rules = new ArrayList<>();

        // Act
        ValidationResult validationResult = profileValidator.validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateProfile_withNullRules_fails()
    {
        // Arrange
        ProfileDTO dto = new ProfileDTO();
        dto.fields = Collections.singletonList(FieldDTOBuilder.create("test"));
        dto.rules = null;

        // Act
        ValidationResult validationResult = profileValidator.validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }
}