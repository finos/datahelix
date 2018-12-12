package com.scottlogic.deg.generator.inputs.validation;

import java.util.ArrayList;
import java.util.List;

class ConstraintRestrictions {

    public final TypeConstraintRestrictions typeConstraintRestrictions;
    public final TemporalConstraintRestrictions temporalConstraintRestrictions;
    public final SetConstraintRestrictions setConstraintRestrictions;
    public final StringConstraintRestrictions stringConstraintRestrictions;
    public final NullConstraintRestrictions nullConstraintRestrictions;
    public final GranularityConstraintRestrictions granularityConstraintRestrictions;

    public ConstraintRestrictions(TypeConstraintRestrictions typeConstraintRestrictions,
                                  TemporalConstraintRestrictions temporalConstraintRestrictions,
                                  SetConstraintRestrictions setConstraintRestrictions,
                                  StringConstraintRestrictions stringConstraintRestrictions,
                                  NullConstraintRestrictions nullConstraintRestrictions,
                                  GranularityConstraintRestrictions granularityConstraintRestrictions)
    {
        this.typeConstraintRestrictions = typeConstraintRestrictions;
        this.temporalConstraintRestrictions = temporalConstraintRestrictions;
        this.setConstraintRestrictions = setConstraintRestrictions;
        this.stringConstraintRestrictions = stringConstraintRestrictions;
        this.nullConstraintRestrictions = nullConstraintRestrictions;
        this.granularityConstraintRestrictions = granularityConstraintRestrictions;
    }

    public List<ValidationAlert> getValidationAlerts(){

        List<ValidationAlert> alerts = new ArrayList<>();

        alerts.addAll(typeConstraintRestrictions.getAlerts());
        alerts.addAll(temporalConstraintRestrictions.getAlerts());
        alerts.addAll(setConstraintRestrictions.getAlerts());
        alerts.addAll(stringConstraintRestrictions.getAlerts());
        alerts.addAll(nullConstraintRestrictions.getAlerts());
        alerts.addAll(granularityConstraintRestrictions.getAlerts());

        return alerts;
    }
}
