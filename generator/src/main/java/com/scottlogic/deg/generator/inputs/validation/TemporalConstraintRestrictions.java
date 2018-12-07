package com.scottlogic.deg.generator.inputs.validation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class TemporalConstraintRestrictions {

    public final ValidationAlert.ValidationType ValidationType = ValidationAlert.ValidationType.TEMPORAL;

    private LocalDateTime min;
    private LocalDateTime max;

    public TemporalConstraintRestrictions(){
        this.min = LocalDateTime.MIN;
        this.max = LocalDateTime.MAX;
    }

    public  List<ValidationAlert> IsAfter(String field, LocalDateTime referenceValue) {
        List<ValidationAlert> alerts = new ArrayList<>();

        if(this.max.compareTo(referenceValue)<0) {
            alerts.add(new ValidationAlert(
                ValidationAlert.Criticality.ERROR,
                String.format("Is after %s is not allowed for this field.", referenceValue.toString()),
                ValidationType,
                field ));
        }

        if(this.min.compareTo(referenceValue)<0) {
            this.min = referenceValue;
        }

        return alerts;
    }

    public  List<ValidationAlert> IsBefore(String field, LocalDateTime referenceValue) {
        List<ValidationAlert> alerts = new ArrayList<>();

        if(this.min.compareTo(referenceValue)>0) {
            alerts.add(new ValidationAlert(
                ValidationAlert.Criticality.ERROR,
                String.format("Is before %s is not allowed for this field.", referenceValue.toString()),
                ValidationType,
                field ));
        }

        if(this.max.compareTo(referenceValue)>0) {
            this.max = referenceValue;
        }

        return alerts;
    }

}
