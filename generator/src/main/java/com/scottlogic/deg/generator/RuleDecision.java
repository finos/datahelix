package com.scottlogic.deg.generator;

import java.util.ArrayList;
import java.util.Collection;

public class RuleDecision {
    public final Collection<RuleOption> options;

    public RuleDecision(Collection<RuleOption> options) {
        this.options = options;
    }

    public RuleDecision(RuleOption optionA, RuleOption optionB) {
        options = new ArrayList<>();
        options.add(optionA);
        options.add(optionB);
    }
}
