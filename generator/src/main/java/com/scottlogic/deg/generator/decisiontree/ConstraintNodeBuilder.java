package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.profile.constraints.delayed.DelayedAtomicConstraint;
import com.scottlogic.deg.common.util.FlatMappingSpliterator;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Stream.concat;

public class ConstraintNodeBuilder {
    private final Collection<AtomicConstraint> atomicConstraints;
    private final Collection<DelayedAtomicConstraint> delayedAtomicConstraints;
    private final Collection<DecisionNode> decisions;
    private final Set<NodeMarking> nodeMarkings;

    public ConstraintNodeBuilder(Collection<AtomicConstraint> atomicConstraints,
                                 Collection<DelayedAtomicConstraint> delayedAtomicConstraints,
                                 Collection<DecisionNode> decisions,
                                 Set<NodeMarking> nodeMarkings) {
        this.atomicConstraints = atomicConstraints;
        this.delayedAtomicConstraints = delayedAtomicConstraints;
        this.decisions = decisions;
        this.nodeMarkings = nodeMarkings;
    }

    public ConstraintNodeBuilder() {
        this(Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptySet());
    }

    public ConstraintNodeBuilder setAtomicConstraints(Collection<AtomicConstraint> newAtomicConstraints) {
        return new ConstraintNodeBuilder(newAtomicConstraints, delayedAtomicConstraints, decisions, nodeMarkings);
    }

    public ConstraintNodeBuilder setDelayedAtomicConstraints(Collection<DelayedAtomicConstraint> newConstraints) {
        return new ConstraintNodeBuilder(atomicConstraints, newConstraints, decisions, nodeMarkings);
    }

    public ConstraintNodeBuilder removeAtomicConstraint(AtomicConstraint atomicConstraint) {
        return setAtomicConstraints(atomicConstraints
            .stream()
            .filter(constraint -> !constraint.equals(atomicConstraint))
            .collect(Collectors.toList())
        );
    }

    public ConstraintNodeBuilder addAtomicConstraints(Collection<AtomicConstraint> atomicConstraints) {
        return setAtomicConstraints(
            concat(
                this.atomicConstraints.stream(),
                atomicConstraints.stream()
            ).collect(Collectors.toList())
        );
    }

    public ConstraintNodeBuilder addAtomicConstraints(AtomicConstraint... constraints) {
        return addAtomicConstraints(Arrays.asList(constraints));
    }

    public ConstraintNodeBuilder addDelayedAtomicConstraints(Collection<DelayedAtomicConstraint> delayedAtomicConstraints) {
        return setDelayedAtomicConstraints(
            concat(
                this.delayedAtomicConstraints.stream(),
                delayedAtomicConstraints.stream())
            .collect(Collectors.toList())
        );
    }

    public ConstraintNodeBuilder addDelayedAtomicConstraints(DelayedAtomicConstraint... constraints) {
        return addDelayedAtomicConstraints(Arrays.asList(constraints));
    }

    public ConstraintNodeBuilder setDecisions(Collection<DecisionNode> newDecisions) {
        return new ConstraintNodeBuilder(atomicConstraints, delayedAtomicConstraints, newDecisions, nodeMarkings);
    }

    public ConstraintNodeBuilder removeDecision(DecisionNode decisionNode) {
        return setDecisions(
            decisions.stream()
                .filter(decision -> !decision.equals(decisionNode))
                .collect(Collectors.toSet()));
    }

    public ConstraintNodeBuilder removeDecisions(Collection<DecisionNode> decisionNodes) {
        ConstraintNodeBuilder constraintNodeBuilder = this;
        for (DecisionNode decisionNode : decisionNodes) {
            constraintNodeBuilder = constraintNodeBuilder.removeDecision(decisionNode);
        }
        return constraintNodeBuilder;
    }

    public ConstraintNodeBuilder addDecision(DecisionNode decisionNode) {
        return setDecisions(
            concat(
                decisions.stream(),
                Stream.of(decisionNode)
            ).collect(Collectors.toList()));
    }

    public ConstraintNodeBuilder addDecisions(Collection<DecisionNode> decisions) {
        return setDecisions(
            concat(
                this.decisions.stream(),
                decisions.stream()
            ).collect(Collectors.toList()));
    }


    public ConstraintNodeBuilder setNodeMarkings(Set<NodeMarking> newNodeMarkings) {
        return new ConstraintNodeBuilder(atomicConstraints, delayedAtomicConstraints, decisions, newNodeMarkings);
    }

    public ConstraintNodeBuilder markNode(NodeMarking marking) {
        return setNodeMarkings(
            FlatMappingSpliterator.flatMap(
                Stream.of(Collections.singleton(marking), nodeMarkings),
                Collection::stream
            ).collect(Collectors.toSet()));
    }

    public ConstraintNode build() {
        return new ConstraintNode(atomicConstraints, delayedAtomicConstraints, decisions, nodeMarkings);
    }

}
