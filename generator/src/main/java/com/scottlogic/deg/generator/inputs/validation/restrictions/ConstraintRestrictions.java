package com.scottlogic.deg.generator.inputs.validation.restrictions;

import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;

import java.util.ArrayList;
import java.util.List;

public class ConstraintRestrictions {

    public final TypeConstraintValidationMergeOperation typeConstraintValidationMergeOperation;
    public final TemporalConstraintRestrictions temporalConstraintRestrictions;
    public final SetConstraintValidationMergeOperation setConstraintValidationMergeOperation;
    public final StringConstraintValidationMergeOperation stringConstraintValidationMergeOperation;
    public final NullConstraintValidationMergeOperation nullConstraintValidationMergeOperation;
    public final GranularityConstraintValidationMergeOperation granularityConstraintValidationMergeOperation;
    public final NumericConstraintValidationMergeOperation numericConstraintRestriction;

    public ConstraintRestrictions(TypeConstraintValidationMergeOperation typeConstraintValidationMergeOperation,
                                  TemporalConstraintRestrictions temporalConstraintRestrictions,
                                  SetConstraintValidationMergeOperation setConstraintValidationMergeOperation,
                                  StringConstraintValidationMergeOperation stringConstraintValidationMergeOperation,
                                  NullConstraintValidationMergeOperation nullConstraintValidationMergeOperation,
                                  GranularityConstraintValidationMergeOperation granularityConstraintValidationMergeOperation,
                                  NumericConstraintValidationMergeOperation numericConstraintRestriction)
    {
        this.typeConstraintValidationMergeOperation = typeConstraintValidationMergeOperation;
        this.temporalConstraintRestrictions = temporalConstraintRestrictions;
        this.setConstraintValidationMergeOperation = setConstraintValidationMergeOperation;
        this.stringConstraintValidationMergeOperation = stringConstraintValidationMergeOperation;
        this.nullConstraintValidationMergeOperation = nullConstraintValidationMergeOperation;
        this.granularityConstraintValidationMergeOperation = granularityConstraintValidationMergeOperation;
        this.numericConstraintRestriction = numericConstraintRestriction;
    }

    public List<ValidationAlert> getValidationAlerts(){

        List<ValidationAlert> alerts = new ArrayList<>();

        alerts.addAll(typeConstraintValidationMergeOperation.getAlerts());
        alerts.addAll(temporalConstraintRestrictions.getAlerts());
        alerts.addAll(setConstraintValidationMergeOperation.getAlerts());
        alerts.addAll(stringConstraintValidationMergeOperation.getAlerts());
        alerts.addAll(nullConstraintValidationMergeOperation.getAlerts());
        alerts.addAll(granularityConstraintValidationMergeOperation.getAlerts());
        alerts.addAll(numericConstraintRestriction.getAlerts());

        return alerts;
    }
}
