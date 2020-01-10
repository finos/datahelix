package com.scottlogic.datahelix.generator.profile.validators.profile.constraints.atomic;

import com.scottlogic.datahelix.generator.common.profile.FieldType;
import com.scottlogic.datahelix.generator.common.profile.StandardSpecificFieldType;
import com.scottlogic.datahelix.generator.common.util.Defaults;
import com.scottlogic.datahelix.generator.common.validators.ValidationResult;
import com.scottlogic.datahelix.generator.profile.creation.FieldDTOBuilder;
import com.scottlogic.datahelix.generator.profile.dtos.FieldDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.integer.OfLengthConstraintDTO;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OfLengthConstraintValidatorTests {

    private final List<FieldDTO> fields = Arrays.asList
        (
            FieldDTOBuilder.create("integer", StandardSpecificFieldType.INTEGER.toSpecificFieldType())
        );

    @Test
    public void validateOfLengthConstraint_withValidData_succeeds()
    {
        // Arrange
        OfLengthConstraintDTO dto = new OfLengthConstraintDTO();
        dto.field = "integer";
        dto.value = 1;

        // Act
        ValidationResult validationResult = new OfLengthConstraintValidator(fields, FieldType.NUMERIC).validate(dto);

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateOfLengthConstraint_withNullField_fails()
    {
        // Arrange
        OfLengthConstraintDTO dto = new OfLengthConstraintDTO();
        dto.field = null;
        dto.value = 1;

        // Act
        ValidationResult validationResult = new OfLengthConstraintValidator(fields, FieldType.NUMERIC).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateOfLengthConstraint_withEmptyField_fails()
    {
        // Arrange
        OfLengthConstraintDTO dto = new OfLengthConstraintDTO();
        dto.field = "";
        dto.value = 1;

        // Act
        ValidationResult validationResult = new OfLengthConstraintValidator(fields, FieldType.NUMERIC).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateOfLengthConstraint_withUndefinedField_fails()
    {
        // Arrange
        OfLengthConstraintDTO dto = new OfLengthConstraintDTO();
        dto.field = "unknown";
        dto.value = 1;

        // Act
        ValidationResult validationResult = new OfLengthConstraintValidator(fields, FieldType.NUMERIC).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateOfLengthConstraint_withValueLessThanMin_fails() {
        // Arrange
        OfLengthConstraintDTO dto = new OfLengthConstraintDTO();
        dto.field = "integer";
        dto.value = -1;

        // Act
        ValidationResult validationResult = new OfLengthConstraintValidator(fields, FieldType.NUMERIC).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateOfLengthConstraint_withValueGreaterThanMax_fails() {
        // Arrange
        OfLengthConstraintDTO dto = new OfLengthConstraintDTO();
        dto.field = "integer";
        dto.value = Defaults.MAX_STRING_LENGTH + 1;

        // Act
        ValidationResult validationResult = new OfLengthConstraintValidator(fields, FieldType.NUMERIC).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }
}
