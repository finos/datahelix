package com.scottlogic.deg.generator.inputs.validation.validators;

import com.scottlogic.deg.generator.inputs.validation.Criticality;
import com.scottlogic.deg.generator.inputs.validation.messages.StandardValidationMessages;
import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;
import com.scottlogic.deg.generator.inputs.validation.ValidationType;
import com.scottlogic.deg.generator.inputs.validation.messages.StringConstraintValidationMessages;

import java.util.ArrayList;
import java.util.List;

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
        } else if (lengthMin > length) {
            logError(field, new StringConstraintValidationMessages(lengthMin, lengthMax, lengthMin, length));
        } else if (lengthMax > length) {
            logError(field, new StringConstraintValidationMessages(lengthMin, lengthMax, lengthMin, length));
        }


    }

    public void isLongerThan(String field, int length) {

        if (length < 0) {
            logError(field, new StringConstraintValidationMessages(lengthMin, lengthMax, length, lengthMax));
            return;
        }

        if (lengthMax > length) {
            lengthMax = length;
        } else if (lengthMin > length) {
            logError(field, new StringConstraintValidationMessages(lengthMin, lengthMax, length, lengthMax));
        } else if (lengthMin < length) {
            if (lengthMax < length) {
                logError(field, new StringConstraintValidationMessages(lengthMin, lengthMax, length, lengthMax));
            } else {
                lengthMin = length;
            }
        }

    }

    public List<ValidationAlert> getAlerts(){
        return alerts;
    }

    private void logError(String field, StandardValidationMessages message){
        alerts.add(new ValidationAlert(
            Criticality.ERROR,
            message,
            validationType,
            field));
    }
}
