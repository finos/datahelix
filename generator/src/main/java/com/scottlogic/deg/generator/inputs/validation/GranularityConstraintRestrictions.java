package com.scottlogic.deg.generator.inputs.validation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GranularityConstraintRestrictions implements ConstraintValidation {

    public final ValidationAlert.ValidationType ValidationType = ValidationAlert.ValidationType.GRANULARITY;

    private BigDecimal granularity;
    private List<ValidationAlert> alerts;

    public GranularityConstraintRestrictions(){
        granularity = BigDecimal.ZERO;
        alerts = new ArrayList<>();
    }

    public void granularTo(String field, BigDecimal referenceValue){

        if(granularity.compareTo(BigDecimal.ZERO) == 0) {
            granularity = referenceValue;
        } else if(granularity.compareTo(referenceValue) > 0){
            granularity = referenceValue;
        } else if(granularity.compareTo(referenceValue) < 0) {
            logError(field, String.format("Granularity is already set to %s. Cannot set it to a higher value: %s", granularity, referenceValue));
        }
    }

    @Override
    public List<ValidationAlert> getAlerts() {
        return alerts;
    }


    private void logError(String field, String message) {
        alerts.add(new ValidationAlert(
            ValidationAlert.Criticality.ERROR,
            message,
            ValidationType,
            field));
    }
}
