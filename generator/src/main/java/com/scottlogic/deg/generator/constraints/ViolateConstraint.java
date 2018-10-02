package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.constraints.IConstraint;

public class ViolateConstraint implements IConstraint {
    public final IConstraint violatedConstraint;

    public ViolateConstraint(IConstraint violatedConstraint) {
        this.violatedConstraint = violatedConstraint;
    }
}
