package com.scottlogic.deg.generator.constraints;

public class ViolateConstraint implements LogicalConstraint {
    public final LogicalConstraint violatedConstraint;

    public ViolateConstraint(LogicalConstraint violatedConstraint) {
        this.violatedConstraint = violatedConstraint;
    }
}
