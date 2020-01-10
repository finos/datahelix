package com.scottlogic.datahelix.generator.profile.validators.profile.constraints.atomic;

import com.scottlogic.datahelix.generator.common.validators.ValidationResult;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DateTimeGranularityValidatorTests {
    
    @Test
    public void validateDateTimeGranularityConstraint_withValidChronoUnit_succeeds()
    {
        // Act
        ValidationResult validationResult = new DateTimeGranularityValidator("errorInfo").validate("millis");

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateDateTimeGranularityConstraint_withWorkingDay_succeeds()
    {
        // Act
        ValidationResult validationResult = new DateTimeGranularityValidator("errorInfo").validate("working days");

        // Assert
        assertTrue(validationResult.isSuccess);
    }

    @Test
    public void validateDateTimeGranularityConstraint_withEmptyValue_fails()
    {
         // Act
        ValidationResult validationResult = new DateTimeGranularityValidator("errorInfo").validate("");

        // Assert
        assertFalse(validationResult.isSuccess);
    }

    @Test
    public void validateDateTimeGranularityConstraint_withInvalidValue_fails()
    {
        // Act
        ValidationResult validationResult = new DateTimeGranularityValidator("errorInfo").validate("mills");

        // Assert
        assertFalse(validationResult.isSuccess);
    }
}
