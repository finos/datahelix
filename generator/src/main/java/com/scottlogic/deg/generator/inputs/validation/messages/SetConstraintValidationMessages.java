package com.scottlogic.deg.generator.inputs.validation.messages;

import com.scottlogic.deg.generator.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.generator.inputs.validation.StandardValidationMessages;

import java.util.Set;

public class SetConstraintValidationMessages implements StandardValidationMessages {


    private Set<Object> allowedValues;
    private Set<Object> invalidValues;

    public SetConstraintValidationMessages(Set<Object> allowedValues, Set<Object> invalidValues){

        this.allowedValues = allowedValues;
        this.invalidValues = invalidValues;
    }

    @Override
    public String getVerboseMessage() {
        return String.format("Set operation with values %s is not allowed. The allowed values are: %s", invalidValues.toString(), allowedValues.toString());
    }
}
