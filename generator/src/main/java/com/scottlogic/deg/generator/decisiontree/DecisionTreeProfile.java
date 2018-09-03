package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.ProfileFields;

import java.util.ArrayList;
import java.util.Collection;

public class DecisionTreeProfile {
    private final ProfileFields fields;
    private final Collection<RuleDecisionTree> rules;

    DecisionTreeProfile(ProfileFields fields, Collection<RuleDecisionTree> rules) {
        this.fields = fields;
        this.rules = rules;
    }

    public ProfileFields getFields() {
        return fields;
    }

    public Collection<RuleDecisionTree> getDecisionTrees() {
        return new ArrayList<>(rules);
    }
}
