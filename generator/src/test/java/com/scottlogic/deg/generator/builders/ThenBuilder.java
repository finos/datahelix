package com.scottlogic.deg.generator.builders;

import com.scottlogic.deg.common.profile.constraints.Constraint;

public class ThenBuilder {
    private final Constraint ifCondition;

    ThenBuilder(BaseConstraintBuilder<? extends Constraint> builder) {
        this.ifCondition = builder.build();
    }

    private ThenBuilder(Constraint ifCondition) {
        this.ifCondition = ifCondition;
    }

    public ElseBuilder withThen(BaseConstraintBuilder<? extends Constraint> builder) {
        return new ElseBuilder(ifCondition, builder);
    }

    public ThenBuilder negate() {
        return new ThenBuilder(ifCondition.negate());
    }
}
