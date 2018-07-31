package com.scottlogic.deg.generator;

import java.util.ArrayList;
import java.util.Collection;

public class AnalysedProfile implements IDecisionTreeProfile {
    private final Collection<Field> fields;
    private final Collection<? extends IRuleDecisionTree> rules;

    public AnalysedProfile(Collection<Field> fields, Collection<? extends IRuleDecisionTree> rules) {
        this.fields = fields;
        this.rules = rules;
    }

    @Override
    public Collection<Field> getFields() {
        return fields;
    }

    @Override
    public Collection<IRuleDecisionTree> getDecisionTrees() {
        return new ArrayList<>(rules);
    }
}
