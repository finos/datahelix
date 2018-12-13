package com.scottlogic.deg.generator.inputs.validation.restrictions;

import com.scottlogic.deg.generator.inputs.validation.Criticality;
import com.scottlogic.deg.generator.inputs.validation.StandardValidationMessages;
import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;
import com.scottlogic.deg.generator.inputs.validation.ValidationType;
import com.scottlogic.deg.generator.inputs.validation.messages.TemporalConstraintValidationMessages;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TemporalConstraintRestrictions implements ConstraintValidation {

    public final ValidationType validationType = ValidationType.TEMPORAL;

    private LocalDateTime min;
    private LocalDateTime max;
    private List<ValidationAlert> alerts;

    public TemporalConstraintRestrictions(){
        this.min = LocalDateTime.MIN;
        this.max = LocalDateTime.MAX;
        this.alerts = new ArrayList<>();
    }

    public void isAfter(String field, LocalDateTime referenceValue) {

        if(this.max.compareTo(referenceValue)<0) {
            logError(field, new TemporalConstraintValidationMessages(min, max, referenceValue, max));
        }

        if(this.min.compareTo(referenceValue)<0) {
            this.min = referenceValue;
        }
    }

    public void isBefore(String field, LocalDateTime referenceValue) {

        if(this.min.compareTo(referenceValue)>0) {
            logError(field, new TemporalConstraintValidationMessages(min, max, min, referenceValue));
        }

        if(this.max.compareTo(referenceValue)>0) {
            this.max = referenceValue;
        }
    }

    private void logError(String field, StandardValidationMessages message){
        alerts.add(new ValidationAlert(
            Criticality.ERROR,
            message,
            validationType,
            field));
    }

    @Override
    public List<ValidationAlert> getAlerts() {
        return alerts;
    }
}
