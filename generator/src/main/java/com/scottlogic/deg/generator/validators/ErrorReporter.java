package com.scottlogic.deg.generator.validators;

import com.scottlogic.deg.common.ValidationException;

public class ErrorReporter {

    public void displayException(Exception e) {
        e.printStackTrace(System.err);
    }

    public void displayValidation(ValidationException e) {
        e.errorMessages.forEach(msg -> System.err.println("* " + msg));
    }
}
