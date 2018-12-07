package com.scottlogic.deg.generator.inputs.visitor;

class ConstraintRestrictions {

    public final TypeConstraintRestrictions typeConstraintRestrictions;
    public final TemporalConstraintRestrictions temporalConstraintRestrictions;

    public ConstraintRestrictions(TypeConstraintRestrictions typeConstraintRestrictions,
                                  TemporalConstraintRestrictions temporalConstraintRestrictions)
    {
        this.typeConstraintRestrictions = typeConstraintRestrictions;
        this.temporalConstraintRestrictions = temporalConstraintRestrictions;
    }
}
