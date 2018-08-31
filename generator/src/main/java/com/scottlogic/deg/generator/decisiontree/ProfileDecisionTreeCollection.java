package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.Rule;

import java.util.*;

public class ProfileDecisionTreeCollection {
    private final List<DecisionTree> rules;
    private final Map<Rule, DecisionTree> ruleToDecisionTree;

    public ProfileDecisionTreeCollection(Map<Rule, DecisionTree> ruleToDecisionTree) {
        this.ruleToDecisionTree = ruleToDecisionTree;
        this.rules = new ArrayList<>(ruleToDecisionTree.values());
    }

    public List<DecisionTree> getDecisionTrees() {
        return this.rules;
    }

    public DecisionTree getForRule(Rule rule) {
        return this.ruleToDecisionTree.get(rule);
    }
}
