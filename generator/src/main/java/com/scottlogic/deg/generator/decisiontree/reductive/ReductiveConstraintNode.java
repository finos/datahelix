package com.scottlogic.deg.generator.decisiontree.reductive;

import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.decisiontree.*;
import com.scottlogic.deg.generator.restrictions.RowSpec;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReductiveConstraintNode implements ConstraintNode {
    private final ConstraintNode underlying;
    private final Collection<AtomicConstraint> unfixedAtomicConstraints;

    public ReductiveConstraintNode(ConstraintNode underlying, Collection<AtomicConstraint> unfixedAtomicConstraints) {
        this.underlying = underlying;
        this.unfixedAtomicConstraints = unfixedAtomicConstraints;
    }

    @Override
    public Collection<AtomicConstraint> getAtomicConstraints() {
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
        return new ReductiveConstraintNode(
            underlying.removeDecisions(decisionsToRemove),
            this.unfixedAtomicConstraints);
    }

    @Override
    public ConstraintNode cloneWithoutAtomicConstraint(AtomicConstraint excludeAtomicConstraint) {
        return new ReductiveConstraintNode(
            underlying.cloneWithoutAtomicConstraint(excludeAtomicConstraint),
            this.unfixedAtomicConstraints);
    }

    @Override
    public boolean atomicConstraintExists(AtomicConstraint constraint) {
        return underlying.atomicConstraintExists(constraint);
    }

    @Override
    public ConstraintNode addAtomicConstraints(Collection<AtomicConstraint> constraints) {
        return new ReductiveConstraintNode(
            underlying.addAtomicConstraints(constraints),
            this.unfixedAtomicConstraints);
    }

    @Override
    public ConstraintNode addDecisions(Collection<DecisionNode> decisions) {
        return new ReductiveConstraintNode(
            underlying.addDecisions(decisions),
            this.unfixedAtomicConstraints);
    }

    @Override
    public ConstraintNode setDecisions(Collection<DecisionNode> decisions) {
        return new ReductiveConstraintNode(
            underlying.setDecisions(decisions),
            this.unfixedAtomicConstraints);
    }

    @Override
    public String toString() {
        return underlying.toString();
    }

    public Collection<AtomicConstraint> getAllUnfixedAtomicConstraints(){
        return this.unfixedAtomicConstraints;
    }

    @Override
    public ConstraintNode markNode(NodeMarking marking) {
        return new ReductiveConstraintNode(
            underlying.markNode(marking),
            this.unfixedAtomicConstraints
        );
    }

    @Override
    public boolean hasMarking(NodeMarking detail) {
        return underlying.hasMarking(detail);
    }

    @Override
    public ConstraintNode accept(NodeVisitor visitor){
        return new ReductiveConstraintNode(underlying.accept(visitor), unfixedAtomicConstraints);
    }
}
