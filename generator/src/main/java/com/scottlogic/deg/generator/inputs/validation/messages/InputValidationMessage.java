package com.scottlogic.deg.generator.inputs.validation.messages;

import java.io.File;

public class InputValidationMessage implements StandardValidationMessages {
    private final String message;
    private final File file;

    public InputValidationMessage(String message, File file) {
        this.message = message;
        this.file = file;
    }

    @Override
    public String getVerboseMessage() {
        return String.format(
            "Invalid Input - %s",
            String.format(
                message,
                file.toString()));
    }
}
