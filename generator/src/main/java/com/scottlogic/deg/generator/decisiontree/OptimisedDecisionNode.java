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
    public DecisionNode addOptions(Collection<ConstraintNode> newOptions){
        return new OptimisedDecisionNode(underlying.addOptions(newOptions));
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof OptimisedDecisionNode)
            o = ((OptimisedDecisionNode)o).underlying;

        return underlying.equals(o);
    }

    @Override
    public int hashCode() {
        return underlying.hashCode();
    }
}
