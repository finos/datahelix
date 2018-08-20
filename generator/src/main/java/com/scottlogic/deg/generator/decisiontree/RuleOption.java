package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.constraints.IConstraint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class RuleOption implements IRuleOption {
    public static RuleOption merge(Iterator<RuleOption> optionsIterator) {
        Collection<IConstraint> atomicConstraints = new ArrayList<>();
        Collection<IRuleDecision> decisions = new ArrayList<>();

        while (optionsIterator.hasNext()) {
            RuleOption option = optionsIterator.next();

            atomicConstraints.addAll(option.atomicConstraints);
            decisions.addAll(option.decisions);
        }

        return new RuleOption(atomicConstraints, decisions);
    }

    private final Collection<IConstraint> atomicConstraints;
    private final Collection<IRuleDecision> decisions;

    public RuleOption(Collection<IConstraint> atomicConstraints, Collection<IRuleDecision> decisions) {
        this.atomicConstraints =  new ArrayList<>(atomicConstraints);
        this.decisions = new ArrayList<>(decisions);
    }

    public RuleOption(IConstraint... atomicConstraints) {
        this(
            Arrays.asList(atomicConstraints),
            new ArrayList<>());
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
