package com.scottlogic.deg.schemas.common;

import java.util.List;

public class ValidationResult {
    public final List<String> errorMessages;

    public ValidationResult(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public boolean isValid() {
        return errorMessages.isEmpty();
    }
}
