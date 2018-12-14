package com.scottlogic.deg.generator.inputs.validation.messages;

import com.scottlogic.deg.generator.inputs.validation.StandardValidationMessages;
import com.scottlogic.deg.generator.restrictions.Nullness;

public class NullConstraintValidationMessages implements StandardValidationMessages {


    private Nullness validValue;
    private Nullness invalidValue;

    public NullConstraintValidationMessages(Nullness validValue, Nullness invalidValue){

        this.validValue = validValue;
        this.invalidValue = invalidValue;
    }

    @Override
    public String getVerboseMessage() {
        return String.format("Having nullness of %s is not valid. The valid nullness value is: %s", invalidValue, validValue);
    }
}
