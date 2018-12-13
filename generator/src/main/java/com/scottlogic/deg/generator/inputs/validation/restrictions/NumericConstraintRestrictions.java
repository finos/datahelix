package com.scottlogic.deg.generator.inputs.validation.restrictions;

import com.scottlogic.deg.generator.inputs.validation.Criticality;
import com.scottlogic.deg.generator.inputs.validation.StandardValidationMessages;
import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;
import com.scottlogic.deg.generator.inputs.validation.ValidationType;
import com.scottlogic.deg.generator.inputs.validation.messages.NumericConstraintValidationMessages;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class NumericConstraintRestrictions implements ConstraintValidation {


    public final ValidationType validationType = ValidationType.NUMERIC;

    private BigDecimal min;
    private BigDecimal max;
    private boolean minInclusive;
    private boolean maxInclusive;
    private List<ValidationAlert> alerts;


    public NumericConstraintRestrictions() {
        this.alerts = new ArrayList<>();
    }

    public void IsLessThan(String field, Number referenceValue) {
        BigDecimal referenceBigDecimal = new BigDecimal(referenceValue.toString());

        if (this.min == null && this.max == null) {
            this.max = referenceBigDecimal;
        } else if (this.max == null && this.min.compareTo(referenceBigDecimal) < 0) {
            this.max = referenceBigDecimal;
        } else if (this.max == null && this.min.compareTo(referenceBigDecimal) >= 0) {
            logError(field, new NumericConstraintValidationMessages(min, max, referenceBigDecimal));
        } else if (this.min == null && this.max.compareTo(referenceBigDecimal) >= 0) {
            this.max = referenceBigDecimal;
        } else if (this.min == null && this.max.compareTo(referenceBigDecimal) < 0) {
            logError(field, new NumericConstraintValidationMessages(min, max, referenceBigDecimal));
        } else if (this.min.compareTo(referenceBigDecimal) > 0) {
            logError(field, new NumericConstraintValidationMessages(min, max, referenceBigDecimal));
        } else if (this.max.compareTo(referenceBigDecimal) > 0) {
            this.max = referenceBigDecimal;
        }

    }

    public void IsGreaterThan(String field, Number referenceValue) {


        BigDecimal referenceBigDecimal = new BigDecimal(referenceValue.toString());

        if (this.min == null && this.max == null) {
            this.min = referenceBigDecimal;
        } else if (this.max == null && this.min.compareTo(referenceBigDecimal) <= 0) {
            this.min = referenceBigDecimal;
        } else if (this.max == null && this.min.compareTo(referenceBigDecimal) > 0) {
            logError(field, new NumericConstraintValidationMessages(min, max, referenceBigDecimal));
        } else if (this.min == null && this.max.compareTo(referenceBigDecimal) >= 0) {
            this.min = referenceBigDecimal;
        } else if (this.min == null && this.max.compareTo(referenceBigDecimal) < 0) {
            logError(field, new NumericConstraintValidationMessages(min, max, referenceBigDecimal));
        } else if (this.max.compareTo(referenceBigDecimal) < 0) {
            logError(field, new NumericConstraintValidationMessages(min, max, referenceBigDecimal));
        } else if (this.min.compareTo(referenceBigDecimal) < 0) {
            this.min = referenceBigDecimal;
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
