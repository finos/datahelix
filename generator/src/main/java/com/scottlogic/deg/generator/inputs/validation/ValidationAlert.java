package com.scottlogic.deg.generator.inputs.validation;


public class ValidationAlert {

    private Criticality criticality;
    private String message;
    private ValidationType validationType;
    private String field;


    public ValidationAlert(
        Criticality criticality,
        String message,
        ValidationType validationType,
        String field){

        this.criticality = criticality;
        this.message = message;
        this.validationType = validationType;
        this.field = field;
    }

    @Override
    public String toString(){
        return String.format("Field %s: %s during %s Validation: %s ", field, criticality.toString(),validationType.toString(), message);
    }

    public Criticality getCriticality(){
        return criticality;
    }

    public enum Criticality {
        ERROR,
        WARNING
    }

    public enum ValidationType {
        TEMPORAL,
        TYPE
    }}

