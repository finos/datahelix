package com.scottlogic.deg.generator.validators;

import com.scottlogic.deg.common.ValidationException;
import com.scottlogic.deg.profile.serialisation.ValidationResult;

public class ErrorReporter {
    public void display(ValidationResult validationResult) {
        validationResult.errorMessages.forEach(e -> System.err.println("* " + e));
    }

    public void displayException(Exception e) {
        e.printStackTrace(System.err);
    }

    public void displayValidation(ValidationException e) {
        e.errorMessages.forEach(msg -> System.err.println("* " + msg));
    }
}
