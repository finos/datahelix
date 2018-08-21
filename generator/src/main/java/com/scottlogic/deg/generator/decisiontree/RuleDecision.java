package com.scottlogic.deg.generator.decisiontree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class RuleDecision {
    private final Collection<? extends RuleOption> options;

    public RuleDecision(Collection<? extends RuleOption> options) {
        this.options = options;
    }

    public RuleDecision(RuleOption... options) {
        this.options = Arrays.asList(options);
    }

    public Collection<RuleOption> getOptions() {
        return new ArrayList<>(options);
    }
}
