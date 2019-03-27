package com.scottlogic.deg.generator.inputs.validation.validators;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.inputs.validation.*;
import com.scottlogic.deg.generator.inputs.validation.messages.*;
import com.scottlogic.deg.generator.restrictions.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class DateTimeConstraintValidator implements ConstraintValidatorAlerts {

    public final ValidationType validationType = ValidationType.DATETIME;

    private final List<ValidationAlert> alerts;
    private DateTimeRestrictions currentRestrictions;

    public DateTimeConstraintValidator() {
        this.alerts = new ArrayList<>();
    }

    public void isAfter(Field field, OffsetDateTime referenceValue, boolean inclusive) {

        DateTimeRestrictions candidateRestrictions = new DateTimeRestrictions();
        candidateRestrictions.min = new DateTimeRestrictions.DateTimeLimit(
            referenceValue,
            inclusive);

        DateTimeRestrictionsMerger merger = new DateTimeRestrictionsMerger();

        MergeResult<DateTimeRestrictions> result = merger.merge(currentRestrictions, candidateRestrictions);

        if (result.successful) {
            currentRestrictions = result.restrictions;

            if (isRangeInvalid() ) {
                logInformation(field, referenceValue);
            }
        } else {
            logInformation(field, referenceValue);
        }
    }

    public void isBefore(Field field, OffsetDateTime referenceValue, boolean inclusive) {

        DateTimeRestrictions candidateRestrictions = new DateTimeRestrictions();
        candidateRestrictions.max = new DateTimeRestrictions.DateTimeLimit(
            referenceValue,
            inclusive);

        DateTimeRestrictionsMerger merger = new DateTimeRestrictionsMerger();

        MergeResult<DateTimeRestrictions> result = merger.merge(currentRestrictions, candidateRestrictions);

        if (result.successful) {
            currentRestrictions = result.restrictions;

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

    private void logError(Field field, OffsetDateTime referenceValue) {
        alerts.add(new ValidationAlert(
            Criticality.ERROR,
            new DateTimeConstraintValidationMessages(
                currentRestrictions,
                referenceValue),
            validationType,
            field));
    }

    private void logInformation(Field field, OffsetDateTime referenceValue) {
        alerts.add(new ValidationAlert(
            Criticality.INFORMATION,
            new DateTimeConstraintValidationMessages(
                currentRestrictions,
                referenceValue),
            validationType,
            field));
    }

    @Override
    public List<ValidationAlert> getAlerts() {
        return alerts;
    }
}
