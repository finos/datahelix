package com.scottlogic.deg.generator.builders;

import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.constraints.grammatical.ConditionalConstraint;

public class IfBuilder {
    public ThenBuilder withIf(ConstraintChainBuilder<? extends Constraint> builder) {
        return new ThenBuilder(builder);
    }

    public ThenBuilder withIf(BaseConstraintBuilder<ConditionalConstraint> builder) {
        return new ThenBuilder(builder);
    }
}
