package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.constraints.IConstraint;

import java.util.ArrayList;
import java.util.Collection;

public class RuleOption {
    public final Collection<IConstraint> atomicConstraints;
    public final Collection<RuleDecision> decisions;

    public RuleOption(Collection<IConstraint> atomicConstraints, Collection<RuleDecision> decisions) {
        this.atomicConstraints =  new ArrayList<>(atomicConstraints);
        this.decisions = new ArrayList<>(decisions);
    }

    public RuleOption(IConstraint singleAtomicConstraint) {
        decisions = new ArrayList<>();
        atomicConstraints = new ArrayList<>();
        atomicConstraints.add(singleAtomicConstraint);
    }

    public RuleOption(RuleDecision singleDecision) {
        atomicConstraints = new ArrayList<>();
        decisions = new ArrayList<>();
        decisions.add(singleDecision);
    }

    public RuleOption merge(RuleOption other) {
        this.atomicConstraints.addAll(other.atomicConstraints);
        this.decisions.addAll(other.decisions);
        return this;
    }
}
