package com.scottlogic.deg.generator.constraints;

public class ViolateConstraint implements LogicalConstraint {
    public final LogicalConstraint violatedConstraint;

    public ViolateConstraint(LogicalConstraint violatedConstraint) {
        this.violatedConstraint = violatedConstraint;
    }

    @Override
    public String toDotLabel() {
        throw new UnsupportedOperationException("VIOLATE constraints should be consumed during conversion to decision trees");
    }
}
