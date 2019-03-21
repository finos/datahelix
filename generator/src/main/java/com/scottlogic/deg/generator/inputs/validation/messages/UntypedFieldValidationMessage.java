package com.scottlogic.deg.generator.inputs.validation.messages;

public class UntypedFieldValidationMessage implements StandardValidationMessages {
    @Override
    public String getVerboseMessage() {
        return "Field is untyped; add an ofType, equalTo or inSet constraint, or mark it as null";
    }
}
