package com.scottlogic.deg.generator.decisiontree.treepartitioning;

import com.scottlogic.deg.common.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;

import java.util.Objects;

class RootLevelConstraint {
    private Object constraint;

    RootLevelConstraint(DecisionNode decisionNode) {
        constraint = decisionNode;
    }

    RootLevelConstraint(AtomicConstraint atomicConstraint) {
        constraint = atomicConstraint;
    }

    DecisionNode getDecisionNode() {
        return constraint instanceof DecisionNode
            ? (DecisionNode)constraint
            : null;
    }

    AtomicConstraint getAtomicConstraint() {
        return constraint instanceof AtomicConstraint
            ? (AtomicConstraint)constraint
            : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RootLevelConstraint that = (RootLevelConstraint) o;
        return Objects.equals(constraint, that.constraint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(constraint);
    }
}
