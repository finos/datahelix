package com.scottlogic.deg.generator.decisiontree.tree_partitioning.test_utils;

import com.scottlogic.deg.generator.decisiontree.DecisionNode;

import java.util.Collection;
import java.util.function.BiConsumer;

public class DecisionComparer implements IEqualityComparer {
    private final AnyOrderCollectionEqualityComparer constraintAnyOrderComparer;
    private final TreeComparisonContext comparisonContext;
    public BiConsumer<Collection, Collection> reportErrors;

    public DecisionComparer(TreeComparisonContext comparisonContext) {
        this.comparisonContext = comparisonContext;
        this.constraintAnyOrderComparer = new AnyOrderCollectionEqualityComparer(
            new ConstraintNodeComparer(comparisonContext, this));
        this.constraintAnyOrderComparer.reportErrors = true;
    }

    public DecisionComparer(ConstraintNodeComparer constraintComparer, TreeComparisonContext comparisonContext) {
        this.constraintAnyOrderComparer = new AnyOrderCollectionEqualityComparer(constraintComparer);
        this.comparisonContext = comparisonContext;
        this.constraintAnyOrderComparer.reportErrors = true;
    }

    @Override
    public int getHashCode(Object decision){
        return getHashCode((DecisionNode)decision);
    }

    public int getHashCode(DecisionNode decision){
        return decision
            .getOptions()
            .stream()
            .reduce(
                0,
                (prev, option) -> prev * option.hashCode(),
                (prevHash, optionHash) -> prevHash * optionHash);
    }

    @Override
    public boolean equals(Object item1, Object item2) {
        return equals((DecisionNode)item1, (DecisionNode)item2);
    }

    public boolean equals(DecisionNode decision1, DecisionNode decision2){
        comparisonContext.setDecision(decision1, decision2);

        if (decision1 == null && decision2 == null)
            return true;

        if (decision1 == null || decision2 == null)
            return false; //either decision1 XOR decision2 is null

        boolean equals = this.constraintAnyOrderComparer.equals(decision1.getOptions(), decision2.getOptions());
        if (!equals && reportErrors != null)
            reportErrors.accept(this.constraintAnyOrderComparer.itemsMissingFromCollection1, this.constraintAnyOrderComparer.itemsMissingFromCollection2);

        return equals;
    }
}
