package com.scottlogic.deg.profile.validators.profile;

import com.scottlogic.deg.common.profile.SpecificFieldType;
import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.profile.creation.ConstraintDTOBuilder;
import com.scottlogic.deg.profile.creation.FieldDTOBuilder;
import com.scottlogic.deg.profile.dtos.FieldDTO;
import com.scottlogic.deg.profile.dtos.constraints.ConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.EqualToConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.grammatical.AllOfConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.relations.EqualToFieldConstraintDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConstraintValidatorTests
{
    private final List<FieldDTO> fields = Arrays.asList
        (
            FieldDTOBuilder.create("text", SpecificFieldType.STRING),
            FieldDTOBuilder.create("integer", SpecificFieldType.INTEGER)
        );

    @Test
    public void validateConstraint_withValidData_succeeds()
    {
        // Arrange
        ConstraintDTO dto = ConstraintDTOBuilder.create("text", "test");

        // Act
        ValidationResult validationResult = ConstraintValidator.validateConstraint(dto, "rule", fields);

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateConstraint_withNullConstraint_fails()
    {
        // Arrange
        ConstraintDTO dto = null;

        // Act
        ValidationResult validationResult = ConstraintValidator.validateConstraint(dto, "rule", fields);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateAtomicConstraint_withNullField_fails()
    {
        // Arrange
        EqualToConstraintDTO dto = new EqualToConstraintDTO();
        dto.field = null;
        dto.value = "value";

        // Act
        ValidationResult validationResult = ConstraintValidator.validateConstraint(dto, "rule", fields);

        // Assert
        assertFalse(validationResult.isSuccess);
    }
    public void validateAtomicConstraint_withEmptyField_fails()
    {
        // Arrange
        EqualToConstraintDTO dto = new EqualToConstraintDTO();
        dto.field = "";
        dto.value = "value";

        // Act
        ValidationResult validationResult = ConstraintValidator.validateConstraint(dto, "rule", fields);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateAtomicConstraint_withUndefinedField_fails()
    {
        // Arrange
        EqualToConstraintDTO dto = new EqualToConstraintDTO();
        dto.field = "unknown";
        dto.value = "value";

        // Act
        ValidationResult validationResult = ConstraintValidator.validateConstraint(dto, "rule", fields);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateAtomicConstraint_withNullValue_fails()
    {
        // Arrange
        EqualToConstraintDTO dto = new EqualToConstraintDTO();
        dto.field = "text";
        dto.value = null;

        // Act
        ValidationResult validationResult = ConstraintValidator.validateConstraint(dto, "rule", fields);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateAtomicConstraint_withIncorrectValueType_fails()
    {
        // Arrange
        EqualToConstraintDTO dto = new EqualToConstraintDTO();
        dto.field = "text";
        dto.value = 1;

        // Act
        ValidationResult validationResult = ConstraintValidator.validateConstraint(dto, "rule", fields);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateGrammaticalConstraint_withNullSubConstraints_fails()
    {
        // Arrange
        AllOfConstraintDTO dto = new AllOfConstraintDTO();
        dto.constraints= null;

        // Act
        ValidationResult validationResult = ConstraintValidator.validateConstraint(dto, "rule", fields);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateGrammaticalConstraint_withEmptySubConstraints_fails()
    {
        // Arrange
        AllOfConstraintDTO dto = new AllOfConstraintDTO();
        dto.constraints = new ArrayList<>();

        // Act
        ValidationResult validationResult = ConstraintValidator.validateConstraint(dto, "rule", fields);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateRelationalConstraint_withNullField_fails()
    {
        // Arrange
        EqualToFieldConstraintDTO dto = new EqualToFieldConstraintDTO();
        dto.field = null;
        dto.otherField = "text";

        // Act
        ValidationResult validationResult = ConstraintValidator.validateConstraint(dto, "rule", fields);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateRelationalConstraint_withEmptyField_fails()
    {
        // Arrange
        EqualToFieldConstraintDTO dto = new EqualToFieldConstraintDTO();
        dto.field = "";
        dto.otherField = "text";

        // Act
        ValidationResult validationResult = ConstraintValidator.validateConstraint(dto, "rule", fields);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateRelationalConstraint_withUndefinedField_fails()
    {
        // Arrange
        EqualToFieldConstraintDTO dto = new EqualToFieldConstraintDTO();
        dto.field = "unknown";
        dto.otherField = "text";

        // Act
        ValidationResult validationResult = ConstraintValidator.validateConstraint(dto, "rule", fields);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateRelationalConstraint_withNullOtherField_fails()
    {
        // Arrange
        EqualToFieldConstraintDTO dto = new EqualToFieldConstraintDTO();
        dto.field = "text";
        dto.otherField = null;

        // Act
        ValidationResult validationResult = ConstraintValidator.validateConstraint(dto, "rule", fields);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateRelationalConstraint_withEmptyOtherField_fails()
    {
        // Arrange
        EqualToFieldConstraintDTO dto = new EqualToFieldConstraintDTO();
        dto.field = "text";
        dto.otherField = "";

        // Act
        ValidationResult validationResult = ConstraintValidator.validateConstraint(dto, "rule", fields);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateRelationalConstraint_withUndefinedOtherField_fails()
    {
        // Arrange
        EqualToFieldConstraintDTO dto = new EqualToFieldConstraintDTO();
        dto.field = "text";
        dto.otherField = "unknown";

        // Act
        ValidationResult validationResult = ConstraintValidator.validateConstraint(dto, "rule", fields);

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateRelationalConstraint_withNonMatchingTypes_fails()
    {
        // Arrange
        EqualToFieldConstraintDTO dto = new EqualToFieldConstraintDTO();
        dto.field = "text";
        dto.otherField = "integer";

        // Act
        ValidationResult validationResult = ConstraintValidator.validateConstraint(dto, "rule", fields);

        // Assert
        assertFalse(validationResult.isSuccess);
    }
}