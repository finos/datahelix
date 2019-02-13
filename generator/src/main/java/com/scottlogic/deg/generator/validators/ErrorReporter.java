package com.scottlogic.deg.generator.validators;

public class ErrorReporter {
    public void display(ValidationResult validationResult) {
        validationResult.errorMessages.forEach(e -> System.err.println("* " + e));
    }
}
