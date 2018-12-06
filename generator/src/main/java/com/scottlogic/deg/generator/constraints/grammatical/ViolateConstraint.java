package com.scottlogic.deg.generator.constraints.grammatical;

import com.scottlogic.deg.generator.constraints.Constraint;

public class ViolateConstraint implements GrammaticalConstraint {
    public final Constraint violatedConstraint;

    public ViolateConstraint(Constraint violatedConstraint) {
        this.violatedConstraint = violatedConstraint;
    }
}
