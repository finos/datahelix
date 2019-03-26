package com.scottlogic.deg.generator.inputs.validation.messages;

public class OutputValidationMessage implements StandardValidationMessages {
    private final String message;

    public OutputValidationMessage(String message) {
        this.message = message;
    }

    @Override
    public String getVerboseMessage() {
        return String.format("Invalid Output - %s", message);
    }
}
