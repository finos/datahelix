package com.scottlogic.deg.generator.validators;

import com.scottlogic.deg.schemas.common.ValidationResult;

public class ErrorReporter {
    public void display(ValidationResult validationResult) {
        validationResult.errorMessages.forEach(e -> System.err.println("* " + e));
    }
}
