package com.scottlogic.deg.generator.inputs.validation.validators;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.restrictions.*;
import com.scottlogic.deg.generator.inputs.validation.*;
import com.scottlogic.deg.generator.inputs.validation.messages.*;

import java.util.ArrayList;
import java.util.List;

public class NullConstraintValidator implements ConstraintValidatorAlerts {

    public final ValidationType validationType = ValidationType.NULL;
    private final List<ValidationAlert> alerts;
    private NullRestrictions currentRestrictions;

    public NullConstraintValidator() {
        this.alerts = new ArrayList<>();
    }


    public void setNullness(Field field, Nullness value) {
        NullRestrictions candidateRestrictions = new NullRestrictions(value);
        NullRestrictionsMerger merger = new NullRestrictionsMerger();

        MergeResult<NullRestrictions> result = merger.merge(currentRestrictions, candidateRestrictions);
        if (result.successful) {
            currentRestrictions = result.restrictions;
        } else {
            logError(field, new NullConstraintValidationMessages(currentRestrictions.nullness, value));
        }
    }


    private void logError(Field field, StandardValidationMessages message) {
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
