package com.scottlogic.deg.generator.builders;

import com.scottlogic.deg.generator.constraints.grammatical.AndConstraint;

public class AndBuilder extends ConstraintChainBuilder<AndConstraint> {
    public AndBuilder() {
        super();
    }

    private AndBuilder(ConstraintChainBuilder<AndConstraint> builder) {
        super(builder);
    }

    public AndConstraint buildInner() {
        return new AndConstraint(constraints);
    }

    @Override
    public ConstraintChainBuilder<AndConstraint> copy() {
        return new AndBuilder(this);
    }
}
