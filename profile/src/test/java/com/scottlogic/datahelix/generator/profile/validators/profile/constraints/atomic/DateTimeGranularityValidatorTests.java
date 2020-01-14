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
