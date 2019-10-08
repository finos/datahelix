package com.scottlogic.deg.generator.walker.rowspec;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;

public class PotentialRowSpecCount {
    private final int max;

    @Inject
    public PotentialRowSpecCount(@Named("config:internalRandomRowSpecStorage") int max) {
        this.max = max;
    }

    public boolean lessThanMax(DecisionTree decisionTree){
        Integer total = count(decisionTree.rootNode);
        return total != null;
    }

    private Integer count(ConstraintNode constraintNode){
        int total = 1;
        for (DecisionNode decision : constraintNode.getDecisions()) {
            Integer count = count(decision);
            if (count == null) return null;

            total *= count;
            if (total > max) return null;
        }
        return total;
    }

    private Integer count(DecisionNode decision) {
        int total = 0;
        for (ConstraintNode option : decision.getOptions()) {
            Integer count = count(option);
            if (count == null) return null;

            total += count;
            if (total > max) return null;
        }
        return total;
    }
}