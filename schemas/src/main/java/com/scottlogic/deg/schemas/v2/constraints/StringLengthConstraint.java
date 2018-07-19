package com.scottlogic.deg.schemas.v2.constraints;

public class StringLengthConstraint extends Constraint {
    public Number min;
    public Number max;

    public StringLengthConstraint()
    {
        super(ConstraintTypes.StringLengthRange);
    }
}
