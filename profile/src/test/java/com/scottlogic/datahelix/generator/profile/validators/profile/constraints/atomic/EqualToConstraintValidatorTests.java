package com.scottlogic.datahelix.generator.profile.validators.profile.constraints.atomic;

import com.scottlogic.datahelix.generator.common.profile.StandardSpecificFieldType;
import com.scottlogic.datahelix.generator.common.validators.ValidationResult;
import com.scottlogic.datahelix.generator.profile.creation.FieldDTOBuilder;
import com.scottlogic.datahelix.generator.profile.dtos.FieldDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.EqualToConstraintDTO;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EqualToConstraintValidatorTests {

    private final List<FieldDTO> fields = Arrays.asList
        (
            FieldDTOBuilder.create("text", StandardSpecificFieldType.STRING.toSpecificFieldType())
        );

    @Test
    public void validateEqualToConstraint_withValidData_succeeds()
    {
        // Arrange
        EqualToConstraintDTO dto = new EqualToConstraintDTO();
        dto.field = "text";
        dto.value = "test";

        // Act
        ValidationResult validationResult = new EqualToConstraintValidator(fields).validate(dto);

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateEqualToConstraint_withNullField_fails()
    {
        // Arrange
        EqualToConstraintDTO dto = new EqualToConstraintDTO();
        dto.field = null;
        dto.value = 1;

        // Act
        ValidationResult validationResult = new EqualToConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateEqualToConstraint_withEmptyField_fails()
    {
        // Arrange
        EqualToConstraintDTO dto = new EqualToConstraintDTO();
        dto.field = "";
        dto.value = 1;

        // Act
        ValidationResult validationResult = new EqualToConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateEqualToConstraint_withUndefinedField_fails()
    {
        // Arrange
        EqualToConstraintDTO dto = new EqualToConstraintDTO();
        dto.field = "unknown";
        dto.value = 1;

        // Act
        ValidationResult validationResult = new EqualToConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateEqualToConstraint_withIncorrectValueType_fails() {
        // Arrange
        EqualToConstraintDTO dto = new EqualToConstraintDTO();
        dto.field = "text";
        dto.value = 1;

        // Act
        ValidationResult validationResult = new EqualToConstraintValidator(fields).validate(dto);

        // Assert
        assertFalse(validationResult.isSuccess);
    }
}
