package com.scottlogic.deg.generator.inputs.validation.messages;

/** A generic StandardValidationMessages implementation */
public class StringValidationMessage implements StandardValidationMessages {
    private final String message;

    public StringValidationMessage(String message) {
        this.message = message;
    }

    @Override
    public String getVerboseMessage() {
        return message;
    }
}
