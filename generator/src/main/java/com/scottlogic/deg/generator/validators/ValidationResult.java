package com.scottlogic.deg.generator.validators;

import java.util.ArrayList;

public class ValidationResult {
    public final ArrayList<String> errorMessages;

    public ValidationResult(ArrayList<String> errorMessages){
        this.errorMessages = errorMessages;
    }

    public boolean isValid() {
        return errorMessages.isEmpty();
    }
}
