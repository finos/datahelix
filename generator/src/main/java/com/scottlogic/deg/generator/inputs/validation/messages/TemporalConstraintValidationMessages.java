package com.scottlogic.deg.generator.inputs.validation.messages;

import com.scottlogic.deg.generator.inputs.validation.StandardValidationMessages;

import java.time.LocalDateTime;

public class TemporalConstraintValidationMessages implements StandardValidationMessages {


    private LocalDateTime allowedMin;
    private LocalDateTime allowedMax;
    private LocalDateTime invalidValue;

    public TemporalConstraintValidationMessages(LocalDateTime allowedMin, LocalDateTime allowedMax, LocalDateTime invalidValue) {

        this.allowedMin = allowedMin;
        this.allowedMax = allowedMax;
        this.invalidValue = invalidValue;
    }

    @Override
    public String getVerboseMessage() {

        return String.format("Temporal constraint with value %s is not allowed. The allowed range is between %s and %s.", invalidValue, allowedMin == null ? "anytime" : allowedMin , allowedMax == null ? "anytime" : allowedMax);
    }
}
