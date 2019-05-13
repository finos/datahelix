package com.scottlogic.deg.generator.validators;

import com.scottlogic.deg.profile.common.ValidationResult;

public class ErrorReporter {
    public void display(ValidationResult validationResult) {
        validationResult.errorMessages.forEach(e -> System.err.println("* " + e));
    }

    public void displayException(Exception e) {
        e.printStackTrace(System.err);
    }
}
