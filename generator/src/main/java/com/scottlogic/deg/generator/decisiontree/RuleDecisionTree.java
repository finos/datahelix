package com.scottlogic.deg.generator.decisiontree;

class RuleDecisionTree implements IRuleDecisionTree {
    private final String description;
    private final IRuleOption root;

    RuleDecisionTree(String description, IRuleOption root) {
        this.description = description;
        this.root = root;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public IRuleOption getRootOption() {
        return root;
    }
}
