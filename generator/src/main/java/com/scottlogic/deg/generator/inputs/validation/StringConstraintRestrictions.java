package com.scottlogic.deg.generator.inputs.validation;

import java.util.ArrayList;
import java.util.List;

public class StringConstraintRestrictions implements ConstraintValidation {

    public final ValidationAlert.ValidationType ValidationType = ValidationAlert.ValidationType.STRING;
    private int lengthMin;
    private int lengthMax;
    private List<ValidationAlert> alerts;

    public StringConstraintRestrictions() {
        this.lengthMin = Integer.MIN_VALUE;
        this.lengthMax = Integer.MAX_VALUE;
        this.alerts = new ArrayList<>();
    }

    public void IsShorterThan(String field, int length) {

        if (length < 0) {
            logError(field, String.format("Is shorter than length must be a non-negative integer. Value %s is not allowed.", length));
            return;
        }

        if (length > lengthMin && length < lengthMax) {
            lengthMax = length;
        } else if (lengthMin > length) {
            logError(field, String.format("Is shorter than %s is not allowed for this field. The allowed range for this field is between %s and %s.", length, lengthMin, lengthMax));
        } else if (lengthMax > length) {
            logError(field, String.format("Is shorter than %s is not allowed for this field. The allowed range for this field is between %s and %s.", length, lengthMin, lengthMax));
        }

        if (lengthMin > lengthMax) {
            logError(field, String.format("Something went wrong whilst validating a Is shorter than restriction. The minimum length is more than the maximum length."));
        }

    }

    public void IsLongerThan(String field, int length) {

        if (length < 0) {
            logError(field, String.format("Is longer than length must be a non-negative integer. Value %s is not allowed.", length));
            return;
        }

        if (lengthMax > length) {
            lengthMax = length;
        } else if (lengthMin > length) {
            logError(field, String.format("Is longer than %s is not allowed for this field. The allowed range for this field is between %s and %s.", length, lengthMin, lengthMax));
        } else if (lengthMin < length) {
            if (lengthMax < length) {
                logError(field, String.format("Is longer than %s is not allowed for this field. The allowed range for this field is between %s and %s.", length, lengthMin, lengthMax));
            } else {
                lengthMin = length;
            }
        }

        if (lengthMin > lengthMax) {
            logError(field, String.format("Something went wrong whilst validating a Is shorter than restriction. The minimum length is more than the maximum length."));
        }

    }

    public List<ValidationAlert> getAlerts(){
        return alerts;
    }

    private void logError(String field, String message){
        alerts.add(new ValidationAlert(
            ValidationAlert.Criticality.ERROR,
            message,
            ValidationType,
            field));
    }
}
