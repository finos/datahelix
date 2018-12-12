package com.scottlogic.deg.generator.inputs.validation;

import com.scottlogic.deg.generator.utils.SetUtils;

import java.util.*;

public class SetConstraintRestrictions implements  ConstraintValidation{

    public final ValidationAlert.ValidationType ValidationType = ValidationAlert.ValidationType.SET;

    private Set<Object> legalValues;
    private List<ValidationAlert> alerts;

    public SetConstraintRestrictions() {
        legalValues = new HashSet<>();
        alerts = new ArrayList<>();
    }

    public void IsInSet(String field, Set<Object> values) {

        // TODO should empty set be allowed value?
        if (legalValues.size() == 0) {
            legalValues = values;
        } else if (values.size() > legalValues.size()) {
            logError(field, String.format("Set values %s do not meet the existing restrictions. Allowed values are: %s", values.toString(), legalValues.toString()));
        } else {
            Set<Object> intersection = SetUtils.intersect(legalValues, values);

            if (intersection.size() < values.size()) {
                logError(field, String.format("Set values %s do not meet the existing restrictions. Allowed values are: %s", values.toString(), legalValues.toString()));
            } else {
                legalValues = intersection;
            }
        }
    }

    private void logError(String field, String message) {
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
