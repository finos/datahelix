package com.scottlogic.deg.generator.decisiontree.tree_partitioning.test_utils;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;

public class ConstraintNodeComparer implements IEqualityComparer {
    private final DecisionComparer decisionComparer;
    private final IEqualityComparer decisionAnyOrderComparer;
    private final IEqualityComparer atomicConstraintAnyOrderComparer = new AnyOrderCollectionEqualityComparer();

    public ConstraintNodeComparer() {
        this.decisionComparer = new DecisionComparer(this);
        this.decisionAnyOrderComparer = new AnyOrderCollectionEqualityComparer(decisionComparer);
    }

    public ConstraintNodeComparer(DecisionComparer decisionComparer) {
        this.decisionComparer = decisionComparer;
        this.decisionAnyOrderComparer = new AnyOrderCollectionEqualityComparer(decisionComparer);
    }

    @Override
    public int getHashCode(Object item) {
        return getHashCode((ConstraintNode)item);
    }

    public int getHashCode(ConstraintNode constraint) {
        int decisionsHashCode = constraint
            .getDecisions()
            .stream()
            .reduce(
                0,
                (prev, decision) -> prev * decisionComparer.getHashCode(decision),
                (prevHash, decisionHash) -> prevHash * decisionHash);

        int atomicConstraintsHashCode = constraint
            .getAtomicConstraints()
            .stream()
            .reduce(
                0,
                (prev, atomicConstraint) -> prev * atomicConstraint.hashCode(),
                (prevHash, atomicConstraintHash) -> prevHash * atomicConstraintHash);

        return decisionsHashCode * atomicConstraintsHashCode;
    }

    @Override
    public boolean equals(Object item1, Object item2) {
        return equals((ConstraintNode)item1, (ConstraintNode)item2);
    }

    public boolean equals(ConstraintNode constraint1, ConstraintNode constraint2) {
        boolean atomicConstraintsMatch = atomicConstraintAnyOrderComparer.equals(constraint1.getAtomicConstraints(), constraint2.getAtomicConstraints());
        if (!atomicConstraintsMatch)
            return false;

        boolean decisionsMatch = decisionAnyOrderComparer.equals(constraint1.getDecisions(), constraint2.getDecisions());
        if (!decisionsMatch)
            return false;

        return true;
    }
}
