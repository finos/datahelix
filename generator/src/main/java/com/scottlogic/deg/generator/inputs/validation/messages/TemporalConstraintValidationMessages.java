package com.scottlogic.deg.generator.inputs.validation.messages;

import com.scottlogic.deg.generator.inputs.validation.StandardValidationMessages;

import java.time.LocalDateTime;

public class TemporalConstraintValidationMessages implements StandardValidationMessages {


    private LocalDateTime validMin;
    private LocalDateTime validMax;
    private LocalDateTime invalidValue;

    public TemporalConstraintValidationMessages(LocalDateTime validMin, LocalDateTime validMax, LocalDateTime invalidValue) {

        this.validMin = validMin;
        this.validMax = validMax;
        this.invalidValue = invalidValue;
    }

    @Override
    public String getVerboseMessage() {

        return String.format("Temporal constraint with value %s is not valid. The valid range is between %s and %s.", invalidValue, validMin == null ? "anytime" : validMin, validMax == null ? "anytime" : validMax);
    }
}
