package com.scottlogic.deg.generator.inputs.validation.messages;


public class StringConstraintValidationMessages implements StandardValidationMessages {


    private int validMinLength;
    private int validMaxLength;
    private int invalidMinLength;
    private int invalidMaxLength;

    public StringConstraintValidationMessages(
        int validMinLength,
        int validMaxLength,
        int invalidMinLength,
        int invalidMaxLength) {

        this.validMinLength = validMinLength;
        this.validMaxLength = validMaxLength;
        this.invalidMinLength = invalidMinLength;
        this.invalidMaxLength = invalidMaxLength;
    }

    @Override
    public String getVerboseMessage() {
        return String.format(
            "String length constraint between %s and %s is not valid. The valid range is between %s and %s.",
            invalidMinLength,
            invalidMaxLength,
            validMinLength,
            validMaxLength);
    }
}
