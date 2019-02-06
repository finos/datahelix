package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.CommandLine.ValidationResult;

public class ErrorReporter {
    public void display(ValidationResult validationResult) {
        validationResult.errorMessages.forEach(e -> System.err.println("* " + e));
    }
}
