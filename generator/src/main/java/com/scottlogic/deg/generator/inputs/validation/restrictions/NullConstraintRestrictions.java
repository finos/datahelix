package com.scottlogic.deg.generator.inputs.validation.restrictions;

import com.scottlogic.deg.generator.inputs.validation.Criticality;
import com.scottlogic.deg.generator.inputs.validation.StandardValidationMessages;
import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;
import com.scottlogic.deg.generator.inputs.validation.ValidationType;
import com.scottlogic.deg.generator.inputs.validation.messages.NullConstraintValidationMessages;

import java.util.ArrayList;
import java.util.List;

public class NullConstraintRestrictions implements ConstraintValidation {


    public final ValidationType validationType = ValidationType.NULL;
    private Nullness nullness;
    private List<ValidationAlert> alerts;


    public NullConstraintRestrictions() {
        this.alerts = new ArrayList<>();
    }

    public void mustBeNull(String field) {
        if (nullness == null) {
            nullness = Nullness.MUST_BE_NULL;
        } else if (nullness.equals(Nullness.MUST_NOT_BE_NULL)) {
            logError(field, new NullConstraintValidationMessages(nullness, Nullness.MUST_BE_NULL));
        } else if (nullness.equals(Nullness.MUST_BE_NULL)) {

        } else {
            logError(field, new NullConstraintValidationMessages(nullness, Nullness.MUST_BE_NULL));
        }
    }

    public void mustNotBeNull(String field) {
        if (nullness == null) {
            nullness = Nullness.MUST_NOT_BE_NULL;
        } else if (nullness.equals(Nullness.MUST_BE_NULL)) {
            logError(field, new NullConstraintValidationMessages(nullness, Nullness.MUST_NOT_BE_NULL));
        } else if (nullness.equals(Nullness.MUST_NOT_BE_NULL)) {

        } else {
            logError(field, new NullConstraintValidationMessages(nullness, Nullness.MUST_NOT_BE_NULL));
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
