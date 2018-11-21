package com.scottlogic.deg.generator.decisiontree;

import java.util.Collection;

public class ContradictoryDecisionNode implements DecisionNode, ContradictoryNode{
    private final DecisionNode underlying;

    public ContradictoryDecisionNode(DecisionNode underlying) {
        this.underlying = underlying;
    }

    @Override
    public Collection<ConstraintNode> getOptions() {
        return underlying.getOptions();
    }

    @Override
    public DecisionNode setOptions(Collection<ConstraintNode> options) {
        return new ContradictoryDecisionNode(underlying.setOptions(options));
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ContradictoryDecisionNode)
            o = ((ContradictoryDecisionNode)o).underlying;

        return underlying.equals(o);
    }

    @Override
    public int hashCode() {
        return underlying.hashCode();
    }

}
