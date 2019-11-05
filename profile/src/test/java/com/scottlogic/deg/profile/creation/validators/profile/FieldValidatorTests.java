package com.scottlogic.deg.profile.creation.validators.profile;

import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.profile.creation.FieldDTOBuilder;
import com.scottlogic.deg.profile.creation.dtos.FieldDTO;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FieldValidatorTests
{

    private final FieldValidator fieldValidator = new FieldValidator();

    @Test
    public void validateField_withValidData_succeeds()
    {
        // Arrange
        FieldDTO dto = FieldDTOBuilder.create("test");

        // Act
        ValidationResult validationResult = fieldValidator.validate(dto);

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateField_withNullField_fails()
    {
        // Arrange
        FieldDTO dto = null;

        // Act
        ValidationResult validationResult = fieldValidator.validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }
    @Test
    public void validateField_withNullName_fails()
    {
        // Arrange
        FieldDTO dto = FieldDTOBuilder.create(null);

        // Act
        ValidationResult validationResult = fieldValidator.validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateField_withEmptyName_fails()
    {
        // Arrange
        FieldDTO dto = FieldDTOBuilder.create("");

        // Act
        ValidationResult validationResult = fieldValidator.validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateField_withNullType_fails()
    {
        // Arrange
        FieldDTO dto = FieldDTOBuilder.create("test", null);

        // Act
        ValidationResult validationResult = fieldValidator.validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }
}