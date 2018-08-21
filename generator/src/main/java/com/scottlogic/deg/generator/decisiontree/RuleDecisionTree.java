package com.scottlogic.deg.generator.decisiontree;

public class RuleDecisionTree {
    private final String description;
    private final RuleOption root;

    RuleDecisionTree(String description, RuleOption root) {
        this.description = description;
        this.root = root;
    }

    public String getDescription() {
        return description;
    }

    public RuleOption getRootOption() {
        return root;
    }
}
