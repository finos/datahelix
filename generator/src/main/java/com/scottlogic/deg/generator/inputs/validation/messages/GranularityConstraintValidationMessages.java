package com.scottlogic.deg.generator.inputs.validation.messages;

import com.scottlogic.deg.generator.inputs.validation.StandardValidationMessages;

import java.math.BigDecimal;

public class GranularityConstraintValidationMessages implements StandardValidationMessages {


    private BigDecimal allowedValue;
    private BigDecimal invalidValue;

    public GranularityConstraintValidationMessages(BigDecimal allowedValue, BigDecimal invalidValue){

        this.allowedValue = allowedValue;
        this.invalidValue = invalidValue;
    }

    @Override
    public String getVerboseMessage() {
        return String.format("Cannot set granularity to %s. Granularity is currently set to: %s", invalidValue, allowedValue);
    }
}

