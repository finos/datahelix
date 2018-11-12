package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.restrictions.RowSpec;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;

public class OptimisedTreeConstraintNode implements ConstraintNode, OptimisedNode{

    private final ConstraintNode underlying;

    public OptimisedTreeConstraintNode(ConstraintNode underlying) {
        this.underlying = underlying;
    }

    @Override
    public Collection<IConstraint> getAtomicConstraints() {
        return underlying.getAtomicConstraints();
    }

    @Override
    public Collection<DecisionNode> getDecisions() {
        return underlying.getDecisions();
    }

    @Override
    public Optional<RowSpec> getOrCreateRowSpec(Supplier<Optional<RowSpec>> createRowSpecFunc) {
        return underlying.getOrCreateRowSpec(createRowSpecFunc);
    }

    @Override
    public void removeDecision(DecisionNode decision) {
        underlying.removeDecision(decision);
    }

    @Override
    public ConstraintNode cloneWithoutAtomicConstraint(IConstraint excludeAtomicConstraint) {
        return underlying.cloneWithoutAtomicConstraint(excludeAtomicConstraint);
    }

    @Override
    public boolean atomicConstraintExists(IConstraint constraint) {
        return underlying.atomicConstraintExists(constraint);
    }

    @Override
    public void addAtomicConstraints(Collection<IConstraint> constraints) {
        underlying.addAtomicConstraints(constraints);
    }

    @Override
    public void appendDecisionNode(DecisionNode decisionNode) {
        underlying.appendDecisionNode(decisionNode);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof OptimisedTreeConstraintNode)
            o = ((OptimisedTreeConstraintNode)o).underlying;

        return underlying.equals(o);
    }

    @Override
    public int hashCode() {
        return underlying.hashCode();
    }
}
