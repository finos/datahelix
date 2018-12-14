package com.scottlogic.deg.generator.inputs.validation.validators;

import com.scottlogic.deg.generator.inputs.validation.Criticality;
import com.scottlogic.deg.generator.inputs.validation.messages.StandardValidationMessages;
import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;
import com.scottlogic.deg.generator.inputs.validation.ValidationType;
import com.scottlogic.deg.generator.inputs.validation.messages.NumericConstraintValidationMessages;
import com.scottlogic.deg.generator.restrictions.MergeResult;
import com.scottlogic.deg.generator.restrictions.NumericLimit;
import com.scottlogic.deg.generator.restrictions.NumericRestrictions;
import com.scottlogic.deg.generator.restrictions.NumericRestrictionsMerger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class NumericConstraintValidator implements ConstraintValidatorAlerts {


    public final ValidationType validationType = ValidationType.NUMERIC;
    private List<ValidationAlert> alerts;
    private NumericRestrictions currentRestrictions;

    public NumericConstraintValidator() {
        this.alerts = new ArrayList<>();
    }

    public void IsLessThan(String field, Number referenceValue, boolean inclusive) {

        BigDecimal referenceBigDecimal = new BigDecimal(referenceValue.toString());
        NumericRestrictions candidateRestrictions = new NumericRestrictions();
        candidateRestrictions.max = new NumericLimit<>(
            referenceBigDecimal,
            inclusive);

        NumericRestrictionsMerger merger = new NumericRestrictionsMerger();
        MergeResult<NumericRestrictions> result = merger.merge(currentRestrictions, candidateRestrictions);

        if(result.successful){
            currentRestrictions = result.restrictions;
        } else {
            logError(field, new NumericConstraintValidationMessages(
                currentRestrictions.min == null ? null : currentRestrictions.min.getLimit(),
                currentRestrictions.max == null ? null : currentRestrictions.max.getLimit(),
                referenceBigDecimal));
        }

    }

    public void IsGreaterThan(String field, Number referenceValue, boolean inclusive) {

        BigDecimal referenceBigDecimal = new BigDecimal(referenceValue.toString());
        NumericRestrictions candidateRestrictions = new NumericRestrictions();
        candidateRestrictions.min = new NumericLimit<>(
            referenceBigDecimal,
            inclusive);

        NumericRestrictionsMerger merger = new NumericRestrictionsMerger();
        MergeResult<NumericRestrictions> result = merger.merge(currentRestrictions, candidateRestrictions);

        if(result.successful){
            currentRestrictions = result.restrictions;
        } else {
            logError(field, new NumericConstraintValidationMessages(
                currentRestrictions.min == null ? null : currentRestrictions.min.getLimit(),
                currentRestrictions.max == null ? null : currentRestrictions.max.getLimit(),
                referenceBigDecimal));
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
