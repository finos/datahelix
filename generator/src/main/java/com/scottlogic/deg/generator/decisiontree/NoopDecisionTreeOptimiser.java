package com.scottlogic.deg.generator.decisiontree;

public class NoopDecisionTreeOptimiser implements IDecisionTreeOptimiser {

    @Override
    public DecisionTree optimiseTree(DecisionTree tree) {
        return tree;
    }
}
