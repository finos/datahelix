package com.scottlogic.deg.generator.inputs.validation.validators;

import com.scottlogic.deg.generator.inputs.validation.Criticality;
import com.scottlogic.deg.generator.inputs.validation.messages.StandardValidationMessages;
import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;
import com.scottlogic.deg.generator.inputs.validation.ValidationType;
import com.scottlogic.deg.generator.inputs.validation.messages.SetConstraintValidationMessages;
import com.scottlogic.deg.generator.restrictions.*;
import com.scottlogic.deg.generator.utils.SetUtils;

import java.util.*;

public class SetConstraintValidator implements ConstraintValidatorAlerts {

    public final ValidationType validationType = ValidationType.SET;

    private List<ValidationAlert> alerts;
    private SetRestrictions currentRestrictions;

    public SetConstraintValidator() {
        alerts = new ArrayList<>();
    }

    public void isInSet(String field, Set<Object> values) {

        SetRestrictions candidateRestrictions = new SetRestrictions(values, null);

        SetRestrictionsMerger merger = new SetRestrictionsMerger();
        MergeResult<SetRestrictions> result = merger.merge(currentRestrictions, candidateRestrictions);

        if (result.successful) {

            //additional validation checks
            if (currentRestrictions != null && currentRestrictions.getWhitelist() != null) {

                Set<Object> intersection = SetUtils.intersect(currentRestrictions.getWhitelist(), values);
                if (intersection.size() < values.size()) {
                    logError(field, new SetConstraintValidationMessages(currentRestrictions.getWhitelist(), values));
                }
            }
            else {
                currentRestrictions = result.restrictions;
            }

        } else {
            logError(field, new SetConstraintValidationMessages(currentRestrictions.getWhitelist(), values));
        }
    }


    public void mustNotBeInSet(String field, Set<Object> values) {

        SetRestrictions candidateRestrictions = new SetRestrictions(null, values);

        SetRestrictionsMerger merger = new SetRestrictionsMerger();
        MergeResult<SetRestrictions> result = merger.merge(currentRestrictions, candidateRestrictions);

        if (result.successful) {
            currentRestrictions = result.restrictions;
        } else {
            logError(field, new SetConstraintValidationMessages(currentRestrictions.getWhitelist(), values));
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
