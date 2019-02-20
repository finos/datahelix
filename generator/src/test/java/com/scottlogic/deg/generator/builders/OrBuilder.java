package com.scottlogic.deg.generator.builders;

import com.scottlogic.deg.generator.constraints.grammatical.OrConstraint;

public class OrBuilder extends ConstraintChainBuilder<OrConstraint> {
    public OrConstraint build() {
        saveConstraint();
        return new OrConstraint(constraints);
    }
}
