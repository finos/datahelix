package com.scottlogic.deg.generator.constraints;

public class ViolateConstraint implements Constraint {
    public final Constraint violatedConstraint;

    public ViolateConstraint(Constraint violatedConstraint) {
        this.violatedConstraint = violatedConstraint;
    }
}
