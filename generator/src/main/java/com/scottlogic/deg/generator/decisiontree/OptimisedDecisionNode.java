package com.scottlogic.deg.generator.decisiontree;

import java.util.Collection;

public class OptimisedDecisionNode implements DecisionNode, OptimisedNode{
    private final DecisionNode underlying;

    public OptimisedDecisionNode(DecisionNode underlying) {
        this.underlying = underlying;
    }

    @Override
    public Collection<ConstraintNode> getOptions() {
        return underlying.getOptions();
    }

    @Override
    public DecisionNode addOption(ConstraintNode newConstraint) {
        return new OptimisedDecisionNode(underlying.addOption(newConstraint));
    }
}
