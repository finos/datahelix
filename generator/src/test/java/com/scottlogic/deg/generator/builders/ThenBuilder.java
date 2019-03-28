package com.scottlogic.deg.generator.builders;

import com.scottlogic.deg.generator.constraints.Constraint;

public class ThenBuilder {
    private final Constraint ifCondition;

    public ThenBuilder(BaseConstraintBuilder<? extends Constraint> builder) {
        ifCondition = builder.build();
    }

    public ElseBuilder withThen(BaseConstraintBuilder<? extends Constraint> builder) {
        return new ElseBuilder(ifCondition, builder);
    }
}
