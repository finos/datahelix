package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.constraints.IConstraint;

import java.util.Collection;

public class AnalysedRule extends RuleOption {
    public final String description;

    public AnalysedRule(String description, Collection<IConstraint> atomicConstraints, Collection<RuleDecision> decisions) {
        super(atomicConstraints, decisions);
        this.description = description;
    }

    public AnalysedRule(String description, RuleOption orig) {
        this(description, orig.atomicConstraints, orig.decisions);
    }
}
