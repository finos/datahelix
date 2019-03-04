package com.scottlogic.deg.generator.builders;

import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.constraints.grammatical.ConditionalConstraint;

public class ThenBuilder {

    private final Constraint ifCondition;

    public ThenBuilder(ConstraintChainBuilder<? extends Constraint> builder) {
        ifCondition = builder.build();
    }

    public ThenBuilder(BaseConstraintBuilder<ConditionalConstraint> builder) {
        ifCondition = builder.buildInner();
    }

    public ElseBuilder withThen(ConstraintChainBuilder<? extends Constraint> builder) {
        return new ElseBuilder(ifCondition, builder);
    }

    public ElseBuilder withThen(BaseConstraintBuilder<ConditionalConstraint> builder) {
        return new ElseBuilder(ifCondition, builder);
    }
}
