package com.scottlogic.deg.generator.inputs.validation.validators;

import com.scottlogic.deg.generator.inputs.validation.*;
import com.scottlogic.deg.generator.inputs.validation.messages.*;


import java.util.ArrayList;
import java.util.List;

//TODO Consider bringing these rules to field spec restrictions
public class StringConstraintValidator implements ConstraintValidatorAlerts {

    public final ValidationType validationType = ValidationType.STRING;
    private int lengthMin;
    private int lengthMax;
    private List<ValidationAlert> alerts;

    public StringConstraintValidator() {
        this.lengthMin = 0;
        this.lengthMax = Integer.MAX_VALUE;
        this.alerts = new ArrayList<>();
    }

    public void isShorterThan(String field, int length) {

        if (length < 0) {
            logError(field, new StringConstraintValidationMessages(lengthMin, lengthMax, lengthMin, length));
            return;
        }

        if (length > lengthMin && length < lengthMax) {
            lengthMax = length;
        } else if (lengthMin > length || lengthMax < length) {
            logError(field, new StringConstraintValidationMessages(lengthMin, lengthMax, lengthMin, length));
        }
    }

    public void isLongerThan(String field, int length) {

        if (length < 0) {
            logError(field, new StringConstraintValidationMessages(lengthMin, lengthMax, length, lengthMax));
            return;
        }

        if (lengthMax > length && lengthMin > length) {
            lengthMin = length;
        } else if (lengthMin < length || lengthMax < length) {
            logError(field, new StringConstraintValidationMessages(lengthMin, lengthMax, length, lengthMax));
        }
    }

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
