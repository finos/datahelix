package com.scottlogic.deg.generator.decisiontree;

public class DecisionTree {
    private final ConstraintNode root;

    DecisionTree(ConstraintNode root) {
        this.root = root;
    }

    public ConstraintNode getRootNode() {
        return root;
    }
}
