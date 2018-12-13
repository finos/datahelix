package com.scottlogic.deg.generator.inputs.validation.restrictions;

import com.scottlogic.deg.generator.inputs.validation.Criticality;
import com.scottlogic.deg.generator.inputs.validation.StandardValidationMessages;
import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;
import com.scottlogic.deg.generator.inputs.validation.ValidationType;
import com.scottlogic.deg.generator.inputs.validation.messages.GranularityConstraintValidationMessages;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GranularityConstraintRestrictions implements ConstraintValidation {

    public final ValidationType validationType = ValidationType.GRANULARITY;

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
            logError(field, new GranularityConstraintValidationMessages(granularity, referenceValue));
        }
    }

    @Override
    public List<ValidationAlert> getAlerts() {
        return alerts;
    }


    private void logError(String field, StandardValidationMessages message) {
        alerts.add(new ValidationAlert(
            Criticality.ERROR,
            message,
            validationType,
            field));
    }
}
