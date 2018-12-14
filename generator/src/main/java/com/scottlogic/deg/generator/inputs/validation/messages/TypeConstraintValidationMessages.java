package com.scottlogic.deg.generator.inputs.validation.messages;

import com.scottlogic.deg.generator.constraints.atomic.IsOfTypeConstraint;

public class TypeConstraintValidationMessages implements StandardValidationMessages {


    private IsOfTypeConstraint.Types validType;
    private IsOfTypeConstraint.Types invalidType;

    public TypeConstraintValidationMessages(IsOfTypeConstraint.Types validType, IsOfTypeConstraint.Types invalidType) {

        this.validType = validType;
        this.invalidType = invalidType;
    }

    @Override
    public String getVerboseMessage() {
        return String.format(
            "Type %s is not valid. The valid type is: %s",
            invalidType,
            validType);
    }
}
