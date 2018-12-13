package com.scottlogic.deg.generator.inputs.validation.messages;

import com.scottlogic.deg.generator.inputs.validation.StandardValidationMessages;
import com.scottlogic.deg.generator.inputs.validation.restrictions.Nullness;

public class NullConstraintValidationMessages implements StandardValidationMessages {


    private Nullness allowedValue;
    private Nullness invalidValue;

    public NullConstraintValidationMessages(Nullness allowedValue, Nullness invalidValue){

        this.allowedValue = allowedValue;
        this.invalidValue = invalidValue;
    }

    @Override
    public String getVerboseMessage() {
        return String.format("Cannot set nullness to %s. Allowed nullness value is: %s", invalidValue, allowedValue);
    }
}
