package com.scottlogic.deg.generator.builders;

import com.scottlogic.deg.common.profile.constraints.Constraint;
import com.scottlogic.deg.common.profile.constraints.grammatical.OrConstraint;

import java.util.List;

public class OrBuilder extends ConstraintChainBuilder<OrConstraint> {
    public OrBuilder() {
        super();
    }

    private OrBuilder(Constraint headConstraint, List<Constraint> tailConstraints) {
        super(headConstraint, tailConstraints);
    }

    public OrConstraint buildInner() {
        return new OrConstraint(tailConstraints);
    }

    @Override
    ConstraintChainBuilder<OrConstraint> create(Constraint headConstraint, List<Constraint> tailConstraints) {
        return new OrBuilder(headConstraint, tailConstraints);
    }
}
