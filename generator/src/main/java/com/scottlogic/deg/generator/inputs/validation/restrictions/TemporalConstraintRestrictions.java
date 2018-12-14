package com.scottlogic.deg.generator.inputs.validation.restrictions;

import com.scottlogic.deg.generator.inputs.validation.Criticality;
import com.scottlogic.deg.generator.inputs.validation.StandardValidationMessages;
import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;
import com.scottlogic.deg.generator.inputs.validation.ValidationType;
import com.scottlogic.deg.generator.inputs.validation.messages.TemporalConstraintValidationMessages;
import com.scottlogic.deg.generator.restrictions.DateTimeRestrictions;
import com.scottlogic.deg.generator.restrictions.DateTimeRestrictionsMerger;
import com.scottlogic.deg.generator.restrictions.MergeResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TemporalConstraintRestrictions implements ConstraintValidation {

    public final ValidationType validationType = ValidationType.TEMPORAL;

    private List<ValidationAlert> alerts;
    DateTimeRestrictions currentRestrictions;

    public TemporalConstraintRestrictions() {
        this.alerts = new ArrayList<>();
    }

    public void isAfter(String field, LocalDateTime referenceValue, boolean inclusive) {

        DateTimeRestrictions candidateRestrictions = new DateTimeRestrictions();
        candidateRestrictions.min = new DateTimeRestrictions.DateTimeLimit(
            referenceValue,
            inclusive);

        DateTimeRestrictionsMerger merger = new DateTimeRestrictionsMerger();

        MergeResult<DateTimeRestrictions> result = merger.merge(currentRestrictions, candidateRestrictions);

        if (result.successful) {
            currentRestrictions = result.restrictions;
        } else {
            logError(field, new TemporalConstraintValidationMessages(
                currentRestrictions.min == null ? null : currentRestrictions.min.getLimit(),
                currentRestrictions.max == null ? null : currentRestrictions.max.getLimit(),
                referenceValue));
        }
    }

    public void isBefore(String field, LocalDateTime referenceValue, boolean inclusive) {

        DateTimeRestrictions candidateRestrictions = new DateTimeRestrictions();
        candidateRestrictions.max = new DateTimeRestrictions.DateTimeLimit(
            referenceValue,
            inclusive);

        DateTimeRestrictionsMerger merger = new DateTimeRestrictionsMerger();

        MergeResult<DateTimeRestrictions> result = merger.merge(currentRestrictions, candidateRestrictions);

        if (result.successful) {
            currentRestrictions = result.restrictions;
        } else {
            logError(field, new TemporalConstraintValidationMessages(
                currentRestrictions.min == null ? null : currentRestrictions.min.getLimit(),
                currentRestrictions.max == null ? null : currentRestrictions.max.getLimit(),
                referenceValue));
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
