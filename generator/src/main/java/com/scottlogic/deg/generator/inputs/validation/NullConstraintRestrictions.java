package com.scottlogic.deg.generator.inputs.validation;

import com.scottlogic.deg.generator.restrictions.NullRestrictions;

import java.util.ArrayList;
import java.util.List;

public class NullConstraintRestrictions implements ConstraintValidation {


    public final ValidationAlert.ValidationType ValidationType = ValidationAlert.ValidationType.NULL;
    private Nullness nullness;
    private List<ValidationAlert> alerts;

    public enum Nullness {
        MUST_BE_NULL,
        MUST_NOT_BE_NULL
    }

    public NullConstraintRestrictions() {
        this.alerts = new ArrayList<>();
    }

    public void mustBeNull(String field) {
        if(nullness == null) {
            nullness = Nullness.MUST_BE_NULL;
        } else if(nullness.equals(Nullness.MUST_NOT_BE_NULL)){
            logError(field, "Cannot set field to null as it already contains a restriction that it must not be null.");
        } else if(nullness.equals(Nullness.MUST_BE_NULL)){

        } else {
            logError(field, String.format("Unexpected value for nullness %s. Please check.", nullness.toString()));
        }
    }

    public void mustNotBeNull(String field) {
        if(nullness == null) {
            nullness = Nullness.MUST_NOT_BE_NULL;
        } else if(nullness.equals(Nullness.MUST_BE_NULL)){
            logError(field, "Cannot set field to must not be null as it already contains a restriction that it must be null.");
        } else if(nullness.equals(Nullness.MUST_NOT_BE_NULL)){

        } else {
            logError(field, String.format("Unexpected value for nullness %s. Please check.", nullness.toString()));
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
