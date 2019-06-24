package com.scottlogic.deg.generator.validators;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;

import java.util.Collection;

public class ContradictionItem {
    private Collection<ConstraintNode> contradictingNodes;
    public ContradictionItem(Collection<ConstraintNode> contradictingNodes) {
        this.contradictingNodes = contradictingNodes;
    }

    public Collection<ConstraintNode> getContradictingNodes() {
        return contradictingNodes;
    }

    public boolean isWhollyContradictory(DecisionTree tree) {
        return this.contradictingNodes.contains(tree.getRootNode());
    }

    public boolean isPartiallyContradictory() {
        return !this.contradictingNodes.isEmpty();
    }
}
