package com.scottlogic.deg.generator.inputs.validation;


import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.inputs.validation.messages.StandardValidationMessages;

public class ValidationAlert {

    private final Criticality criticality;
    private final StandardValidationMessages message;
    private final ValidationType validationType;
    private final Field field;

    public ValidationAlert(
        Criticality criticality,
        StandardValidationMessages message,
        ValidationType validationType,
        Field field){

        this.criticality = criticality;
        this.message = message;
        this.validationType = validationType;
        this.field = field;
    }

    public Criticality getCriticality(){
        return criticality;
    }
    public StandardValidationMessages getMessage(){
        return message;
    }
    public ValidationType getValidationType(){
        return validationType;
    }
    public Field getField(){
        return field;
    }
}

