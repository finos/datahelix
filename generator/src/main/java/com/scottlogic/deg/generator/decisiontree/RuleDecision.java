package com.scottlogic.deg.generator.decisiontree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class RuleDecision implements IRuleDecision {
    private final Collection<? extends IRuleOption> options;

    public RuleDecision(Collection<? extends IRuleOption> options) {
        this.options = options;
    }

    public RuleDecision(IRuleOption... options) {
        this.options = Arrays.asList(options);
    }

    @Override
    public Collection<IRuleOption> getOptions() {
        return new ArrayList<>(options);
    }
}
