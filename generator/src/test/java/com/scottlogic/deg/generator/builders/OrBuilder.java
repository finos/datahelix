package com.scottlogic.deg.generator.builders;

import com.scottlogic.deg.generator.constraints.grammatical.OrConstraint;

public class OrBuilder extends ConstraintChainBuilder<OrConstraint> {
    public OrBuilder() {
        super();
    }

    private OrBuilder(ConstraintChainBuilder<OrConstraint> orBuilder) {
        super(orBuilder);
    }

    public OrConstraint buildInner() {
        return new OrConstraint(constraints);
    }

    @Override
    public ConstraintChainBuilder<OrConstraint> copy() {
        return new OrBuilder(this);
    }
}
