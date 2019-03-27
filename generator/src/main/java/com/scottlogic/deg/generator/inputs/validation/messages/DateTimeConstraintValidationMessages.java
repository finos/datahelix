package com.scottlogic.deg.generator.inputs.validation.messages;

import com.scottlogic.deg.generator.restrictions.DateTimeRestrictions;

import java.time.OffsetDateTime;

public class DateTimeConstraintValidationMessages implements StandardValidationMessages {


    private DateTimeRestrictions restriction;
    private OffsetDateTime newValue;

    public DateTimeConstraintValidationMessages(
        DateTimeRestrictions restriction,
        OffsetDateTime newValue) {

        this.restriction = restriction;
        this.newValue = newValue;
    }

    @Override
    public String getVerboseMessage() {

        return String.format(
            "DateTime constraint with value %s has been applied. The range is %s.",
            newValue,
            restriction.toString());
    }
}
