package com.scottlogic.deg.generator;

import java.util.Collection;

public class AnalysedProfile implements IAnalysedProfile {
    private Collection<Field> fields;
    private Collection<AnalysedRule> rules;

    public AnalysedProfile(Collection<Field> fields, Collection<AnalysedRule> rules) {
        this.fields = fields;
        this.rules = rules;
    }

    @Override
    public Collection<Field> getFields() {
        return fields;
    }

    public Collection<AnalysedRule> getAnalysedRules() {
        return rules;
    }
}
