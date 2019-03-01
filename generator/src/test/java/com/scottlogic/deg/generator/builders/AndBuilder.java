package com.scottlogic.deg.generator.builders;

import com.scottlogic.deg.generator.constraints.grammatical.AndConstraint;

public class AndBuilder extends ConstraintChainBuilder<AndConstraint> {
    public AndConstraint buildInner() {
        return new AndConstraint(constraints);
    }
}
