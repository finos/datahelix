package com.scottlogic.deg.generator.inputs.validation.restrictions;

import com.scottlogic.deg.generator.inputs.validation.Criticality;
import com.scottlogic.deg.generator.inputs.validation.StandardValidationMessages;
import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;
import com.scottlogic.deg.generator.inputs.validation.ValidationType;
import com.scottlogic.deg.generator.inputs.validation.messages.SetConstraintValidationMessages;
import com.scottlogic.deg.generator.utils.SetUtils;

import java.util.*;

public class SetConstraintRestrictions implements ConstraintValidation {

    public final ValidationType validationType = ValidationType.SET;

    private Set<Object> legalValues;
    private List<ValidationAlert> alerts;

    public SetConstraintRestrictions() {
        legalValues = new HashSet<>();
        alerts = new ArrayList<>();
    }

    public void isInSet(String field, Set<Object> values) {

        // TODO should empty set be allowed value?
        if (legalValues.size() == 0) {
            legalValues = values;
        } else if (values.size() > legalValues.size()) {
            logError(field, new SetConstraintValidationMessages(legalValues, values));
        } else {
            Set<Object> intersection = SetUtils.intersect(legalValues, values);

            if (intersection.size() < values.size()) {
                logError(field, new SetConstraintValidationMessages(legalValues, values));
            } else {
                legalValues = intersection;
            }
        }
    }


    public void mustNotBeInSet(String field, Set<Object> values) {

        if (legalValues.size() == 0) {
            return;
        } else {
            Set<Object> intersection = SetUtils.intersect(values, legalValues);

            if (intersection.size() != values.size()) {
                logError(field,  new SetConstraintValidationMessages(legalValues, values));
            } else {
                legalValues = intersection;
            }
        }
    }

    private void logError(String field, StandardValidationMessages message) {
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
