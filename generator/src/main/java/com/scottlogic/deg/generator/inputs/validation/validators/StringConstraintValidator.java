package com.scottlogic.deg.generator.inputs.validation.validators;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.inputs.validation.*;
import com.scottlogic.deg.generator.inputs.validation.messages.*;


import java.util.ArrayList;
import java.util.List;

//TODO Consider bringing these rules to field spec restrictions
public class StringConstraintValidator implements ConstraintValidatorAlerts {

    public final ValidationType validationType = ValidationType.STRING;
    private int lengthMin;
    private int lengthMax;
    private final List<ValidationAlert> alerts;

    public StringConstraintValidator() {
        this.lengthMin = 0;
        this.lengthMax = Integer.MAX_VALUE;
        this.alerts = new ArrayList<>();
    }

    public void isShorterThan(Field field, int length) {

        if (length < 0) {
            logInformation(field, new StringConstraintValidationMessages(lengthMin, lengthMax, lengthMin, length));
            return;
        }

        if(lengthMin <= length && length <= lengthMax) {
            lengthMax = length;
        } else if (length <= lengthMin) {
            logInformation(field, new StringConstraintValidationMessages(lengthMin, lengthMax, lengthMin, length));
        } else if (lengthMax <= length) {
            logInformation(field, new StringConstraintValidationMessages(lengthMin, lengthMax, lengthMin, length));
        }
    }

    public void isLongerThan(Field field, int length) {

        if (length < 0) {
            logInformation(field, new StringConstraintValidationMessages(lengthMin, lengthMax, length, lengthMax));
            return;
        }

        if(lengthMin <= length && length <= lengthMax) {
            lengthMin = length;
        } else if (length <= lengthMin) {
            logInformation(field, new StringConstraintValidationMessages(lengthMin, lengthMax, lengthMin, length));
        } else if (lengthMax <= length) {
            logInformation(field, new StringConstraintValidationMessages(lengthMin, lengthMax, lengthMin, length));
        }
    }

    public List<ValidationAlert> getAlerts() {
        return alerts;
    }

    private void logInformation(Field field, StandardValidationMessages message) {
        alerts.add(new ValidationAlert(
            Criticality.INFORMATION,
            message,
            validationType,
            field));
    }
}
