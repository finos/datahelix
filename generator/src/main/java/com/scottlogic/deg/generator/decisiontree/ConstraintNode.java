package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.common.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
        Set<NodeMarking> markings = new HashSet<>();

        while (constraintNodeIterator.hasNext()) {
            ConstraintNode constraintNode = constraintNodeIterator.next();

            atomicConstraints.addAll(constraintNode.getAtomicConstraints());
            decisions.addAll(constraintNode.getDecisions());
            markings.addAll(constraintNode.getNodeMarkings());
        }

        return new TreeConstraintNode(atomicConstraints, decisions, markings);
    }

    default Set<NodeMarking> getNodeMarkings(){
        return Arrays.stream(NodeMarking.values())
            .filter(this::hasMarking)
            .collect(Collectors.toSet());
    }

    @Override
    default Node getFirstChild() {
        return getDecisions().stream().findFirst().orElse(null);
    }
}

