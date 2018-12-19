package com.scottlogic.deg.generator.inputs.validation.messages;

import com.scottlogic.deg.generator.restrictions.NumericRestrictions;

import java.math.BigDecimal;

public class NumericConstraintValidationMessages implements StandardValidationMessages {


    private NumericRestrictions restrictions;
    private BigDecimal newValue;

    public NumericConstraintValidationMessages(NumericRestrictions restrictions, BigDecimal newValue) {

        this.restrictions = restrictions;
        this.newValue = newValue;
    }

    @Override
    public String getVerboseMessage() {

        return String.format(
            "Numeric constraint with value %s has been applied. The range is %s.",
            newValue,
            restrictions.toString());
    }
}
