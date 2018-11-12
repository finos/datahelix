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
    public ConstraintNode removeDecisions(Collection<DecisionNode> decisionsToRemove) {
        return new OptimisedTreeConstraintNode(underlying.removeDecisions(decisionsToRemove));
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
    public ConstraintNode addAtomicConstraints(Collection<IConstraint> constraints) {
        return new OptimisedTreeConstraintNode(underlying.addAtomicConstraints(constraints));
    }

    @Override
    public ConstraintNode addDecisions(Collection<DecisionNode> decisions) {
        return new OptimisedTreeConstraintNode(underlying.addDecisions(decisions));
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
