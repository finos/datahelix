package com.scottlogic.deg.generator.constraints.gramatical;

import com.scottlogic.deg.generator.constraints.Constraint;

public class ViolateConstraint implements GramaticalConstraint {
    public final Constraint violatedConstraint;

    public ViolateConstraint(Constraint violatedConstraint) {
        this.violatedConstraint = violatedConstraint;
    }
}
