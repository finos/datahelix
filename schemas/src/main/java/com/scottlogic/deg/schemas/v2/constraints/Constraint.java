package com.scottlogic.deg.schemas.v2.constraints;

public abstract class Constraint {
    public ConstraintTypes type;

    protected Constraint(ConstraintTypes type)
    {
        this.type = type;
    }
}
