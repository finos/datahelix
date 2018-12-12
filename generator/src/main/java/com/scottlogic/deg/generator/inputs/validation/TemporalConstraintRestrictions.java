package com.scottlogic.deg.generator.inputs.validation;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class TemporalConstraintRestrictions implements ConstraintValidation {

    public final ValidationAlert.ValidationType ValidationType = ValidationAlert.ValidationType.TEMPORAL;

    private LocalDateTime min;
    private LocalDateTime max;
    private List<ValidationAlert> alerts;

    public TemporalConstraintRestrictions(){
        this.min = LocalDateTime.MIN;
        this.max = LocalDateTime.MAX;
        this.alerts = new ArrayList<>();
    }

    public  void isAfter(String field, LocalDateTime referenceValue) {

        if(this.max.compareTo(referenceValue)<0) {
            logError(field, String.format("Is after %s is not allowed for this field.", referenceValue.toString()));
        }

        if(this.min.compareTo(referenceValue)<0) {
            this.min = referenceValue;
        }
    }

    public void isBefore(String field, LocalDateTime referenceValue) {

        if(this.min.compareTo(referenceValue)>0) {
            logError(field, String.format("Is before %s is not allowed for this field.", referenceValue.toString()));
        }

        if(this.max.compareTo(referenceValue)>0) {
            this.max = referenceValue;
        }
    }

    private void logError(String field, String message){
        alerts.add(new ValidationAlert(
            ValidationAlert.Criticality.ERROR,
            message,
            ValidationType,
            field));
    }

    @Override
    public List<ValidationAlert> getAlerts() {
        return alerts;
    }
}
