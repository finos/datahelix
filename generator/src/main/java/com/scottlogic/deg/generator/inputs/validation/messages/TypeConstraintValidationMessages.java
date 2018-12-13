package com.scottlogic.deg.generator.inputs.validation.messages;

import com.scottlogic.deg.generator.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.generator.inputs.validation.StandardValidationMessages;

public class TypeConstraintValidationMessages implements StandardValidationMessages {


    private IsOfTypeConstraint.Types allowedType;
    private IsOfTypeConstraint.Types invalidType;

    public TypeConstraintValidationMessages(IsOfTypeConstraint.Types allowedType, IsOfTypeConstraint.Types invalidType){

        this.allowedType = allowedType;
        this.invalidType = invalidType;
    }

    @Override
    public String getVerboseMessage() {
        return String.format("Type %s is not allowed. The allowed type is: %s", invalidType, allowedType);
    }
}
