package com.scottlogic.deg.generator.constraints;

public class NotConstraint implements IConstraint
{
    public final IConstraint negatedConstraint;

    public NotConstraint(IConstraint negatedConstraint) {
        this.negatedConstraint = negatedConstraint;
    }
}
