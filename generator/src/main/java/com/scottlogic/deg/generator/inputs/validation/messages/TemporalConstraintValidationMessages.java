package com.scottlogic.deg.generator.inputs.validation.messages;

import com.scottlogic.deg.generator.restrictions.DateTimeRestrictions;

import java.time.LocalDateTime;

public class TemporalConstraintValidationMessages implements StandardValidationMessages {


    private DateTimeRestrictions restriction;
    private LocalDateTime newValue;

    public TemporalConstraintValidationMessages(
        DateTimeRestrictions restriction,
        LocalDateTime newValue) {

        this.restriction = restriction;
        this.newValue = newValue;
    }

    @Override
    public String getVerboseMessage() {

        return String.format(
            "Temporal constraint with value %s has been applied. The range is %s.",
            newValue,
            restriction.toString());
    }
}
