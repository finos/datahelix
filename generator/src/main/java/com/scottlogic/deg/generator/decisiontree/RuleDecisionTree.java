package com.scottlogic.deg.generator.decisiontree;

public class RuleDecisionTree {
    private final String description;
    private final ConstraintNode root;

    RuleDecisionTree(String description, ConstraintNode root) {
        this.description = description;
        this.root = root;
    }

    public String getDescription() {
        return description;
    }

    public ConstraintNode getRootNode() {
        return root;
    }
}
