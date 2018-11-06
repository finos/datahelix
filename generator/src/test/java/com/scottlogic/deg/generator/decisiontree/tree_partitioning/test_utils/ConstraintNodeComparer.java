package com.scottlogic.deg.generator.decisiontree.tree_partitioning.test_utils;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;

public class ConstraintNodeComparer implements IEqualityComparer {
    private final DecisionComparer decisionComparer;
    private final AnyOrderCollectionEqualityComparer decisionAnyOrderComparer;
    private final AnyOrderCollectionEqualityComparer atomicConstraintAnyOrderComparer = new AnyOrderCollectionEqualityComparer();
    private final TreeComparisonContext comparisonContext;

    public ConstraintNodeComparer(TreeComparisonContext comparisonContext) {
        this.comparisonContext = comparisonContext;
        this.decisionComparer = new DecisionComparer(this, comparisonContext);
        this.decisionAnyOrderComparer = new AnyOrderCollectionEqualityComparer(decisionComparer);

        this.decisionAnyOrderComparer.reportErrors = true;
        this.atomicConstraintAnyOrderComparer.reportErrors = true;
    }

    public ConstraintNodeComparer(TreeComparisonContext comparisonContext, DecisionComparer decisionComparer) {
        this.comparisonContext = comparisonContext;
        this.decisionComparer = decisionComparer;
        this.decisionAnyOrderComparer = new AnyOrderCollectionEqualityComparer(decisionComparer);

        this.decisionAnyOrderComparer.reportErrors = true;
        this.atomicConstraintAnyOrderComparer.reportErrors = true;
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
        this.comparisonContext.setConstraint(constraint1, constraint2);

        boolean atomicConstraintsMatch = atomicConstraintAnyOrderComparer.equals(constraint1.getAtomicConstraints(), constraint2.getAtomicConstraints());
        if (!atomicConstraintsMatch) {
            this.comparisonContext.reportAtomicConstraintDifferences(
                atomicConstraintAnyOrderComparer.itemsMissingFromCollection1,
                atomicConstraintAnyOrderComparer.itemsMissingFromCollection2);
            return false;
        }

        boolean decisionsMatch = decisionAnyOrderComparer.equals(constraint1.getDecisions(), constraint2.getDecisions());
        if (!decisionsMatch) {
            this.comparisonContext.reportDecisionDifferences(
                decisionAnyOrderComparer.itemsMissingFromCollection1,
                decisionAnyOrderComparer.itemsMissingFromCollection2);
            return false;
        }

        return true;
    }
}
