package com.scottlogic.deg.generator.decisiontree;

public class NoopDecisionTreeOptimiser implements DecisionTreeOptimiser {

    @Override
    public DecisionTree optimiseTree(DecisionTree tree) {
        return tree;
    }
}
