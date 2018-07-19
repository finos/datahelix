package com.scottlogic.deg.schemas.v2.constraints;

public class NumericRangeConstraint extends Constraint {
    public Number min;
    public Number max;

    public NumericRangeConstraint()
    {
        super(ConstraintTypes.NumericRange);
    }
}
