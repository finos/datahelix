package com.scottlogic.deg.generator;

import java.util.Collection;

public class AnalysedProfile implements IAnalysedProfile {
    private final Collection<Field> fields;
    private final Collection<AnalysedRule> rules;

    public AnalysedProfile(Collection<Field> fields, Collection<AnalysedRule> rules) {
        this.fields = fields;
        this.rules = rules;
    }

    @Override
    public Collection<Field> getFields() {
        return fields;
    }

    @Override
    public Collection<AnalysedRule> getAnalysedRules() {
        return rules;
    }
}
