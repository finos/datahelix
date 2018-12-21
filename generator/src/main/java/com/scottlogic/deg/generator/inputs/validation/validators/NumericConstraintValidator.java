package com.scottlogic.deg.generator.inputs.validation.validators;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.restrictions.*;
import com.scottlogic.deg.generator.inputs.validation.*;
import com.scottlogic.deg.generator.inputs.validation.messages.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class NumericConstraintValidator implements ConstraintValidatorAlerts {


    public final ValidationType validationType = ValidationType.NUMERIC;
    private final List<ValidationAlert> alerts;
    private NumericRestrictions currentRestrictions;

    public NumericConstraintValidator() {
        this.alerts = new ArrayList<>();
    }

    public void IsLessThan(Field field, Number referenceValue, boolean inclusive) {

        BigDecimal referenceBigDecimal = new BigDecimal(referenceValue.toString());
        NumericRestrictions candidateRestrictions = new NumericRestrictions();
        candidateRestrictions.max = new NumericLimit<>(
            referenceBigDecimal,
            inclusive);

        NumericRestrictionsMerger merger = new NumericRestrictionsMerger();
        MergeResult<NumericRestrictions> result = merger.merge(currentRestrictions, candidateRestrictions);

        if (result.successful) {
            currentRestrictions = result.restrictions;

            if (isRangeInvalid()) {

                logInformation(field,referenceBigDecimal);
            }

        }
    }

    public void IsGreaterThan(Field field, Number referenceValue, boolean inclusive) {

        BigDecimal referenceBigDecimal = new BigDecimal(referenceValue.toString());
        NumericRestrictions candidateRestrictions = new NumericRestrictions();
        candidateRestrictions.min = new NumericLimit<>(
            referenceBigDecimal,
            inclusive);

        NumericRestrictionsMerger merger = new NumericRestrictionsMerger();
        MergeResult<NumericRestrictions> result = merger.merge(currentRestrictions, candidateRestrictions);

        if (result.successful) {
            currentRestrictions = result.restrictions;

            if (isRangeInvalid()) {
                logInformation(field, referenceBigDecimal);
            }
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


    private void logInformation(Field field, BigDecimal referenceBigDecimal) {
        alerts.add(new ValidationAlert(
            Criticality.INFORMATION,
            new NumericConstraintValidationMessages(
                currentRestrictions,
                referenceBigDecimal),
            validationType,
            field));
    }

    @Override
    public List<ValidationAlert> getAlerts() {
        return alerts;
    }
}
