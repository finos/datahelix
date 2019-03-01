package com.scottlogic.deg.generator.builders;

import com.scottlogic.deg.generator.constraints.Constraint;

public class IfBuilder {
    public ThenBuilder withIf(ConstraintChainBuilder<? extends Constraint> builder) {
        return new ThenBuilder(builder);
    }
}
