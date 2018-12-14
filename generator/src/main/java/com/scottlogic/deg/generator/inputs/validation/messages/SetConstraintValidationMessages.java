package com.scottlogic.deg.generator.inputs.validation.messages;

import com.scottlogic.deg.generator.inputs.validation.StandardValidationMessages;

import java.util.Set;

public class SetConstraintValidationMessages implements StandardValidationMessages {


    private Set<Object> validValues;
    private Set<Object> invalidValues;

    public SetConstraintValidationMessages(Set<Object> validValues, Set<Object> invalidValues){

        this.validValues = validValues;
        this.invalidValues = invalidValues;
    }

    @Override
    public String getVerboseMessage() {
        return String.format("Set operation with values %s is not valid. The valid values are: %s", invalidValues.toString(), validValues.toString());
    }
}
