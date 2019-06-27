package com.scottlogic.deg.generator.builders;

import com.scottlogic.deg.common.profile.constraints.Constraint;
import com.scottlogic.deg.common.profile.constraints.grammatical.AndConstraint;

import java.util.List;

public class AndBuilder extends ConstraintChainBuilder<AndConstraint> {
    public AndBuilder() {
        super();
    }

    private AndBuilder(Constraint headConstraint, List<Constraint> tailConstraints) {
        super(headConstraint, tailConstraints);
    }

    public AndConstraint buildInner() {
        return new AndConstraint(tailConstraints);
    }

    @Override
    ConstraintChainBuilder<AndConstraint> create(Constraint headConstraint, List<Constraint> tailConstraints) {
        return new AndBuilder(headConstraint, tailConstraints);
    }
}
