package com.scottlogic.deg.generator.inputs.validation.validators;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.inputs.validation.*;
import com.scottlogic.deg.generator.inputs.validation.messages.*;
import com.scottlogic.deg.generator.restrictions.GranularityRestrictions;
import com.scottlogic.deg.generator.restrictions.ParsedGranularity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GranularityConstraintValidator implements ConstraintValidatorAlerts {

    public final ValidationType validationType = ValidationType.GRANULARITY;

    private GranularityRestrictions currentRestrictions;
    private final List<ValidationAlert> alerts;

    public GranularityConstraintValidator(){
        alerts = new ArrayList<>();
    }

    public void granularTo(Field field, BigDecimal referenceValue){

        ParsedGranularity parsedReferenceValue = ParsedGranularity.parse(referenceValue);
        GranularityRestrictions candidateRestrictions = new GranularityRestrictions(parsedReferenceValue);

        GranularityRestrictions result = GranularityRestrictions.merge(currentRestrictions, candidateRestrictions);

        if (currentRestrictions != null && currentRestrictions.getNumericScale() != result.getNumericScale()) {
            // Alert information that granularity has changed. This is allowed but may be unexpected to the user.
            logInformation(field, new GranularityConstraintValidationMessages(
                currentRestrictions.getNumericScale(),
                parsedReferenceValue.getNumericGranularity().scale()));
        }

        currentRestrictions = result;
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
