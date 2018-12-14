package com.scottlogic.deg.generator.inputs.validation;


import com.scottlogic.deg.generator.inputs.validation.messages.StandardValidationMessages;

public class ValidationAlert {

    private Criticality criticality;
    private StandardValidationMessages message;
    private ValidationType validationType;
    private String field;

    public ValidationAlert(
        Criticality criticality,
        StandardValidationMessages message,
        ValidationType validationType,
        String field){

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
    public String getField(){
        return field;
    }
}

