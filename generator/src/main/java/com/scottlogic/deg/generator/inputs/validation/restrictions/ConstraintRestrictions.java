package com.scottlogic.deg.generator.inputs.validation.restrictions;

import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;

import java.util.ArrayList;
import java.util.List;

public class ConstraintRestrictions {

    public final TypeConstraintRestrictions typeConstraintRestrictions;
    public final TemporalConstraintRestrictions temporalConstraintRestrictions;
    public final SetConstraintRestrictions setConstraintRestrictions;
    public final StringConstraintRestrictions stringConstraintRestrictions;
    public final NullConstraintRestrictions nullConstraintRestrictions;
    public final GranularityConstraintRestrictions granularityConstraintRestrictions;
    public final NumericConstraintRestrictions numericConstraintRestriction;

    public ConstraintRestrictions(TypeConstraintRestrictions typeConstraintRestrictions,
                                  TemporalConstraintRestrictions temporalConstraintRestrictions,
                                  SetConstraintRestrictions setConstraintRestrictions,
                                  StringConstraintRestrictions stringConstraintRestrictions,
                                  NullConstraintRestrictions nullConstraintRestrictions,
                                  GranularityConstraintRestrictions granularityConstraintRestrictions,
                                  NumericConstraintRestrictions numericConstraintRestriction)
    {
        this.typeConstraintRestrictions = typeConstraintRestrictions;
        this.temporalConstraintRestrictions = temporalConstraintRestrictions;
        this.setConstraintRestrictions = setConstraintRestrictions;
        this.stringConstraintRestrictions = stringConstraintRestrictions;
        this.nullConstraintRestrictions = nullConstraintRestrictions;
        this.granularityConstraintRestrictions = granularityConstraintRestrictions;
        this.numericConstraintRestriction = numericConstraintRestriction;
    }

    public List<ValidationAlert> getValidationAlerts(){

        List<ValidationAlert> alerts = new ArrayList<>();

        alerts.addAll(typeConstraintRestrictions.getAlerts());
        alerts.addAll(temporalConstraintRestrictions.getAlerts());
        alerts.addAll(setConstraintRestrictions.getAlerts());
        alerts.addAll(stringConstraintRestrictions.getAlerts());
        alerts.addAll(nullConstraintRestrictions.getAlerts());
        alerts.addAll(granularityConstraintRestrictions.getAlerts());
        alerts.addAll(numericConstraintRestriction.getAlerts());

        return alerts;
    }
}
