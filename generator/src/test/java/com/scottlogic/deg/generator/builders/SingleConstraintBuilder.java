package com.scottlogic.deg.generator.builders;

import com.scottlogic.deg.generator.constraints.Constraint;

import java.util.List;

public class SingleConstraintBuilder extends ConstraintChainBuilder<Constraint> {
    public SingleConstraintBuilder() { }

    private SingleConstraintBuilder(Constraint headConstraint, List<Constraint> tailConstraints) {
        super(headConstraint, tailConstraints);
    }

    @Override
    ConstraintChainBuilder<Constraint> create(Constraint headConstraint, List<Constraint> tailConstraints) {
        return new SingleConstraintBuilder(headConstraint, tailConstraints);
    }

    @Override
    Constraint buildInner() {
        if (tailConstraints.size() == 0) {
            throw new RuntimeException("Unable to build single constraint, no constraints specified.");
        }

        if (tailConstraints.size() > 1) {
            throw new RuntimeException("Unable to build single constraint, more than 1 constraint specified.");
        }

        return tailConstraints.get(0);
    }
}
