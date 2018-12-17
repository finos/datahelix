package com.scottlogic.deg.generator.inputs.validation.messages;

import java.math.BigDecimal;

public class GranularityConstraintValidationMessages implements StandardValidationMessages {


    private BigDecimal validValue;
    private BigDecimal invalidValue;

    public GranularityConstraintValidationMessages(BigDecimal validValue, BigDecimal invalidValue) {

        this.validValue = validValue;
        this.invalidValue = invalidValue;
    }

    @Override
    public String getVerboseMessage() {
        return String.format(
            "Attempted to set granularity to %s. Granularity is currently set to: %s",
            invalidValue,
            validValue);
    }
}

