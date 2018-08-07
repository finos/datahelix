package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;

import java.util.ArrayList;
import java.util.Collection;

class DecisionTreeProfile implements IDecisionTreeProfile {
    private final ProfileFields fields;
    private final Collection<? extends IRuleDecisionTree> rules;

    DecisionTreeProfile(ProfileFields fields, Collection<? extends IRuleDecisionTree> rules) {
        this.fields = fields;
        this.rules = rules;
    }

    @Override
    public ProfileFields getFields() {
        return fields;
    }

    @Override
    public Collection<IRuleDecisionTree> getDecisionTrees() {
        return new ArrayList<>(rules);
    }
}
