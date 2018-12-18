package com.scottlogic.deg.generator.inputs.validation.validators;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.inputs.validation.*;
import com.scottlogic.deg.generator.inputs.validation.messages.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

//TODO Consider bringing these rules to field spec restrictions
public class GranularityConstraintValidator implements ConstraintValidatorAlerts {

    public final ValidationType validationType = ValidationType.GRANULARITY;

    private BigDecimal granularity;
    private final List<ValidationAlert> alerts;

    public GranularityConstraintValidator(){
        granularity = BigDecimal.ZERO;
        alerts = new ArrayList<>();
    }

    public void granularTo(Field field, BigDecimal referenceValue){

        if(granularity.compareTo(BigDecimal.ZERO) == 0) {
            granularity = referenceValue;
        } else if(granularity.compareTo(referenceValue) > 0){
            granularity = referenceValue;
        } else if(granularity.compareTo(referenceValue) < 0) {
            logInformation(field, new GranularityConstraintValidationMessages(granularity, referenceValue));
        }
    }

    private void logInformation(Field field, StandardValidationMessages message) {
        alerts.add(new ValidationAlert(
            Criticality.INFORMATION,
            message,
            validationType,
            field));
    }

    @Override
    public List<ValidationAlert> getAlerts() {
        return alerts;
    }

}
