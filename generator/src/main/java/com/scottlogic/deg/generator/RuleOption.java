package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.constraints.IConstraint;

import java.util.ArrayList;
import java.util.Collection;

class RuleOption implements IRuleOption {
    private final Collection<IConstraint> atomicConstraints;
    private final Collection<IRuleDecision> decisions;

    RuleOption(Collection<IConstraint> atomicConstraints, Collection<IRuleDecision> decisions) {
        this.atomicConstraints =  new ArrayList<>(atomicConstraints);
        this.decisions = new ArrayList<>(decisions);
    }

    RuleOption(IConstraint singleAtomicConstraint) {
        decisions = new ArrayList<>();
        atomicConstraints = new ArrayList<>();
        atomicConstraints.add(singleAtomicConstraint);
    }

    RuleOption(IRuleDecision singleDecision) {
        atomicConstraints = new ArrayList<>();
        decisions = new ArrayList<>();
        decisions.add(singleDecision);
    }

    @Override
    public Collection<IConstraint> getAtomicConstraints() {
        return new ArrayList<>(atomicConstraints);
    }

    @Override
    public Collection<IRuleDecision> getDecisions() {
        return new ArrayList<>(decisions);
    }

    RuleOption merge(RuleOption other) {
        this.atomicConstraints.addAll(other.atomicConstraints);
        this.decisions.addAll(other.decisions);
        return this;
    }
}
