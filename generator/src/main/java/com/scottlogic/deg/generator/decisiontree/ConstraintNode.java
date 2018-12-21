package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.restrictions.RowSpec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Supplier;

public interface ConstraintNode extends Node {
    Collection<AtomicConstraint> getAtomicConstraints();
    Collection<DecisionNode> getDecisions();
    Optional<RowSpec> getOrCreateRowSpec(Supplier<Optional<RowSpec>> createRowSpecFunc);
    ConstraintNode removeDecisions(Collection<DecisionNode> decisionsToRemove);
    ConstraintNode cloneWithoutAtomicConstraint(AtomicConstraint excludeAtomicConstraint);
    boolean atomicConstraintExists(AtomicConstraint constraint);
    ConstraintNode addAtomicConstraints(Collection<AtomicConstraint> constraints);
    ConstraintNode addDecisions(Collection<DecisionNode> decisions);
    ConstraintNode setDecisions(Collection<DecisionNode> decisions);
    ConstraintNode markNode(NodeMarking marking);
    ConstraintNode accept(NodeVisitor visitor);

    static ConstraintNode merge(Iterator<ConstraintNode> constraintNodeIterator) {
        Collection<AtomicConstraint> atomicConstraints = new ArrayList<>();
        Collection<DecisionNode> decisions = new ArrayList<>();

        while (constraintNodeIterator.hasNext()) {
            ConstraintNode constraintNode = constraintNodeIterator.next();

            atomicConstraints.addAll(constraintNode.getAtomicConstraints());
            decisions.addAll(constraintNode.getDecisions());
        }

        return new TreeConstraintNode(atomicConstraints, decisions);
    }
}

