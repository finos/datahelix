package com.scottlogic.deg.generator.builders;

import com.scottlogic.deg.common.profile.constraints.Constraint;

public class IfBuilder {
    public ThenBuilder withIf(BaseConstraintBuilder<? extends Constraint> builder) {
        return new ThenBuilder(builder);
    }
}
