package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.constraints.IConstraint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public final class ConstraintNode {
    public static ConstraintNode merge(Iterator<ConstraintNode> constraintNodeIterator) {
        Collection<IConstraint> atomicConstraints = new ArrayList<>();
        Collection<DecisionNode> decisions = new ArrayList<>();

        while (constraintNodeIterator.hasNext()) {
            ConstraintNode constraintNode = constraintNodeIterator.next();

            atomicConstraints.addAll(constraintNode.atomicConstraints);
            decisions.addAll(constraintNode.decisions);
        }

        return new ConstraintNode(atomicConstraints, decisions);
    }

    private final Collection<IConstraint> atomicConstraints;
    private final Collection<DecisionNode> decisions;

    public ConstraintNode(Collection<IConstraint> atomicConstraints, Collection<DecisionNode> decisions) {
        this.atomicConstraints =  new ArrayList<>(atomicConstraints);
        this.decisions = new ArrayList<>(decisions);
    }

    public ConstraintNode(IConstraint... atomicConstraints) {
        this(
            Arrays.asList(atomicConstraints),
            new ArrayList<>());
    }

    public ConstraintNode(IConstraint singleAtomicConstraint) {
        decisions = new ArrayList<>();
        atomicConstraints = new ArrayList<>();
        atomicConstraints.add(singleAtomicConstraint);
    }

    public Collection<IConstraint> getAtomicConstraints() {
        return new ArrayList<>(atomicConstraints);
    }

    public Collection<DecisionNode> getDecisions() {
        return new ArrayList<>(decisions);
    }
}
