package com.scottlogic.deg.generator.builders;

import com.scottlogic.deg.generator.constraints.Constraint;

public class ThenBuilder {

    private final Constraint ifCondition;

    public ThenBuilder(ConstraintChainBuilder<? extends Constraint> builder) {
        ifCondition = builder.copy().build();
    }

    public ThenBuilder(ElseBuilder builder) {
        ifCondition = builder.copy().buildInner();
    }

    public ElseBuilder withThen(ConstraintChainBuilder<? extends Constraint> builder) {
        return new ElseBuilder(ifCondition, builder);
    }

    public ElseBuilder withThen(ElseBuilder builder) {
        return new ElseBuilder(ifCondition, builder);
    }
}
