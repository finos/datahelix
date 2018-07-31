package com.scottlogic.deg.generator;

import java.util.ArrayList;
import java.util.Collection;

class RuleDecision implements IRuleDecision {
    private final Collection<? extends IRuleOption> options;

    RuleDecision(Collection<? extends IRuleOption> options) {
        this.options = options;
    }

    RuleDecision(IRuleOption optionA, IRuleOption optionB) {
        ArrayList<IRuleOption> options = new ArrayList<>();
        options.add(optionA);
        options.add(optionB);
        this.options = options;
    }

    @Override
    public Collection<IRuleOption> getOptions() {
        return new ArrayList<>(options);
    }
}
