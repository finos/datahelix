package com.scottlogic.deg.generator.inputs.validation.messages;

import com.scottlogic.deg.generator.inputs.validation.StandardValidationMessages;

import java.math.BigDecimal;

public class NumericConstraintValidationMessages implements StandardValidationMessages {


    private BigDecimal allowedMin;
    private BigDecimal allowedMax;
    private BigDecimal invalidValue;

    public NumericConstraintValidationMessages(BigDecimal allowedMin, BigDecimal allowedMax, BigDecimal invalidValue) {

        this.allowedMin = allowedMin;
        this.allowedMax = allowedMax;
        this.invalidValue = invalidValue;
    }

    @Override
    public String getVerboseMessage() {

        return String.format("Numeric constraint with value %s is invalid. The valid range is between %s and %s.", invalidValue, allowedMin == null ? "anything" : allowedMin, allowedMax == null ? "more" : allowedMax );
    }
}
