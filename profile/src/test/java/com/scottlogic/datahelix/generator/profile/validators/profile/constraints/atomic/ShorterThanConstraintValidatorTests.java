package com.scottlogic.datahelix.generator.profile.validators.profile.constraints.atomic;

import com.scottlogic.datahelix.generator.common.profile.FieldType;
import com.scottlogic.datahelix.generator.common.profile.StandardSpecificFieldType;
import com.scottlogic.datahelix.generator.common.util.Defaults;
import com.scottlogic.datahelix.generator.common.validators.ValidationResult;
import com.scottlogic.datahelix.generator.profile.creation.FieldDTOBuilder;
import com.scottlogic.datahelix.generator.profile.dtos.FieldDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.integer.ShorterThanConstraintDTO;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ShorterThanConstraintValidatorTests {

    private final List<FieldDTO> fields = Arrays.asList
        (
            FieldDTOBuilder.create("integer", StandardSpecificFieldType.INTEGER.toSpecificFieldType())
        );

    @Test
    public void validateShorterThanConstraint_withValidData_succeeds()
    {
        // Arrange
        ShorterThanConstraintDTO dto = new ShorterThanConstraintDTO();
        dto.field = "integer";
        dto.value = Defaults.MAX_STRING_LENGTH + 1;

        // Act
        ValidationResult validationResult = new ShorterThanConstraintValidator(fields, FieldType.NUMERIC).validate(dto);

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateShorterThanConstraint_withNullField_fails()
    {
        // Arrange
        ShorterThanConstraintDTO dto = new ShorterThanConstraintDTO();
        dto.field = null;
        dto.value = 1;

        // Act
        ValidationResult validationResult = new ShorterThanConstraintValidator(fields, FieldType.NUMERIC).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateShorterThanConstraint_withEmptyField_fails()
    {
        // Arrange
        ShorterThanConstraintDTO dto = new ShorterThanConstraintDTO();
        dto.field = "";
        dto.value = 1;

        // Act
        ValidationResult validationResult = new ShorterThanConstraintValidator(fields, FieldType.NUMERIC).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateShorterThanConstraint_withUndefinedField_fails()
    {
        // Arrange
        ShorterThanConstraintDTO dto = new ShorterThanConstraintDTO();
        dto.field = "unknown";
        dto.value = 1;

        // Act
        ValidationResult validationResult = new ShorterThanConstraintValidator(fields, FieldType.NUMERIC).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateShorterThanConstraint_withValueLessThanMin_fails() {
        // Arrange
        ShorterThanConstraintDTO dto = new ShorterThanConstraintDTO();
        dto.field = "integer";
        dto.value = -1;

        // Act
        ValidationResult validationResult = new ShorterThanConstraintValidator(fields, FieldType.NUMERIC).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }
}
