package com.scottlogic.deg.generator.inputs.validation.messages;

import com.scottlogic.deg.generator.inputs.validation.StandardValidationMessages;


public class StringConstraintValidationMessages  implements StandardValidationMessages {


    private int allowedMinLength;
    private int allowedMaxLength;
    private int invalidMinLength;
    private int invalidMaxLength;

    public StringConstraintValidationMessages(int allowedMinLength, int allowedMaxLength, int invalidMinLength, int invalidMaxLength) {

        this.allowedMinLength = allowedMinLength;
        this.allowedMaxLength = allowedMaxLength;
        this.invalidMinLength = invalidMinLength;
        this.invalidMaxLength = invalidMaxLength;
    }

    @Override
    public String getVerboseMessage() {
        return String.format("String length constraint between %s and %s is not allowed. The allowed range is between %s and %s.", invalidMinLength, invalidMaxLength, allowedMinLength, allowedMaxLength);
    }
}
