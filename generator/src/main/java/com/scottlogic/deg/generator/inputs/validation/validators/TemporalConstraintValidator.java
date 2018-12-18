package com.scottlogic.deg.generator.inputs.validation.validators;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.inputs.validation.*;
import com.scottlogic.deg.generator.inputs.validation.messages.*;
import com.scottlogic.deg.generator.restrictions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TemporalConstraintValidator implements ConstraintValidatorAlerts {

    public final ValidationType validationType = ValidationType.TEMPORAL;

    private final List<ValidationAlert> alerts;
    private DateTimeRestrictions currentRestrictions;

    public TemporalConstraintValidator() {
        this.alerts = new ArrayList<>();
    }

    public void isAfter(Field field, LocalDateTime referenceValue, boolean inclusive) {

        DateTimeRestrictions candidateRestrictions = new DateTimeRestrictions();
        candidateRestrictions.min = new DateTimeRestrictions.DateTimeLimit(
            referenceValue,
            inclusive);

        DateTimeRestrictionsMerger merger = new DateTimeRestrictionsMerger();

        DateTimeRestrictions result = merger.merge(currentRestrictions, candidateRestrictions);

        if (result != null) {
            currentRestrictions = result;

            if (isRangeInvalid() ) {
                logInformation(field, referenceValue);
            }
        } else {
            logInformation(field, referenceValue);
        }
    }

    public void isBefore(Field field, LocalDateTime referenceValue, boolean inclusive) {

        DateTimeRestrictions candidateRestrictions = new DateTimeRestrictions();
        candidateRestrictions.max = new DateTimeRestrictions.DateTimeLimit(
            referenceValue,
            inclusive);

        DateTimeRestrictionsMerger merger = new DateTimeRestrictionsMerger();

        DateTimeRestrictions result = merger.merge(currentRestrictions, candidateRestrictions);

        if (result != null) {
            currentRestrictions = result;

            if (isRangeInvalid() ) {
                logInformation(field, referenceValue);
            }
        } else {
            logInformation(field, referenceValue);
        }
    }

    private boolean isRangeInvalid() {
        return currentRestrictions.min != null
            && currentRestrictions.max != null &&
            (currentRestrictions.min.getLimit().compareTo(currentRestrictions.max.getLimit()) > 0
                || ((!currentRestrictions.min.isInclusive()
                || !currentRestrictions.max.isInclusive())
                && currentRestrictions.min.getLimit().compareTo(currentRestrictions.max.getLimit()) == 0));
    }

    private void logError(Field field, LocalDateTime referenceValue) {
        alerts.add(new ValidationAlert(
            Criticality.ERROR,
            new TemporalConstraintValidationMessages(
                currentRestrictions.min == null ? null : currentRestrictions.min.getLimit(),
                currentRestrictions.max == null ? null : currentRestrictions.max.getLimit(),
                referenceValue),
            validationType,
            field));
    }

    private void logInformation(Field field, LocalDateTime referenceValue) {
        alerts.add(new ValidationAlert(
            Criticality.INFORMATION,
            new TemporalConstraintValidationMessages(
                currentRestrictions.min == null ? null : currentRestrictions.min.getLimit(),
                currentRestrictions.max == null ? null : currentRestrictions.max.getLimit(),
                referenceValue),
            validationType,
            field));
    }

    @Override
    public List<ValidationAlert> getAlerts() {
        return alerts;
    }
}
