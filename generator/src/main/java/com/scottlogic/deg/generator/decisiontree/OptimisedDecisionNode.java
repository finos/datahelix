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
    public void addOption(ConstraintNode newConstraint) {
        underlying.addOption(newConstraint);
    }
}
