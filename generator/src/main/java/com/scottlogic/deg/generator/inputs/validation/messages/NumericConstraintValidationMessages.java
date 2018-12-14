package com.scottlogic.deg.generator.inputs.validation.messages;

import com.scottlogic.deg.generator.inputs.validation.StandardValidationMessages;

import java.math.BigDecimal;

public class NumericConstraintValidationMessages implements StandardValidationMessages {


    private BigDecimal validMin;
    private BigDecimal validMax;
    private BigDecimal invalidValue;

    public NumericConstraintValidationMessages(BigDecimal validMin, BigDecimal validMax, BigDecimal invalidValue) {

        this.validMin = validMin;
        this.validMax = validMax;
        this.invalidValue = invalidValue;
    }

    @Override
    public String getVerboseMessage() {

        return String.format("Numeric constraint with value %s is not valid. The valid range is between %s and %s.", invalidValue, validMin == null ? "anything" : validMin, validMax == null ? "more" : validMax);
    }
}
