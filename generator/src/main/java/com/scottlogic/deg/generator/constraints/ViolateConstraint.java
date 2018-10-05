package com.scottlogic.deg.generator.constraints;

public class ViolateConstraint implements IConstraint {
    public final IConstraint violatedConstraint;

    public ViolateConstraint(IConstraint violatedConstraint) {
        this.violatedConstraint = violatedConstraint;
    }

    @Override
    public String toDotLabel() {
        throw new UnsupportedOperationException("VIOLATE constraints should be consumed during conversion to decision trees");
    }
}
