package com.scottlogic.deg.generator.inputs.validation.validators;

import com.scottlogic.deg.generator.inputs.validation.Criticality;
import com.scottlogic.deg.generator.inputs.validation.messages.StandardValidationMessages;
import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;
import com.scottlogic.deg.generator.inputs.validation.ValidationType;
import com.scottlogic.deg.generator.inputs.validation.messages.NullConstraintValidationMessages;
import com.scottlogic.deg.generator.restrictions.MergeResult;
import com.scottlogic.deg.generator.restrictions.NullRestrictions;
import com.scottlogic.deg.generator.restrictions.NullRestrictionsMerger;
import com.scottlogic.deg.generator.restrictions.Nullness;

import java.util.ArrayList;
import java.util.List;

public class NullConstraintValidator implements ConstraintValidatorAlerts {

    public final ValidationType validationType = ValidationType.NULL;
    private List<ValidationAlert> alerts;
    private NullRestrictions currentRestrictions;

    public NullConstraintValidator() {
        this.alerts = new ArrayList<>();
    }


    public void setNullness(String field, Nullness value) {
        NullRestrictions candidateRestrictions = new NullRestrictions(value);
        NullRestrictionsMerger merger = new NullRestrictionsMerger();

        MergeResult<NullRestrictions> result = merger.merge(currentRestrictions, candidateRestrictions);
        if (result.successful) {
            currentRestrictions = result.restrictions;
        } else {
            logError(field, new NullConstraintValidationMessages(currentRestrictions.nullness, value));
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
