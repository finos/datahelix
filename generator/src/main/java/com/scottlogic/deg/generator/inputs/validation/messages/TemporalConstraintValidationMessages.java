package com.scottlogic.deg.generator.inputs.validation.messages;

import com.scottlogic.deg.generator.inputs.validation.StandardValidationMessages;

import java.time.LocalDateTime;

public class TemporalConstraintValidationMessages implements StandardValidationMessages {


    private LocalDateTime allowedMin;
    private LocalDateTime allowedMax;
    private LocalDateTime invalidMin;
    private LocalDateTime invalidMax;

    public TemporalConstraintValidationMessages(LocalDateTime allowedMin, LocalDateTime allowedMax, LocalDateTime invalidMin, LocalDateTime invalidMax) {

        this.allowedMin = allowedMin;
        this.allowedMax = allowedMax;
        this.invalidMin = invalidMin;
        this.invalidMax = invalidMax;
    }

    @Override
    public String getVerboseMessage() {

        return String.format("Temporal constraint between %s and %s is not allowed. The allowed range is between %s and %s.", invalidMin, invalidMax, allowedMin == LocalDateTime.MIN ? "anytime" : allowedMin , allowedMax == LocalDateTime.MAX ? "anytime" : allowedMax);
    }
}
