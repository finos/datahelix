package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.common.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.common.util.FlatMappingSpliterator;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Stream.concat;

public class ConstraintNodeBuilder {
    private Collection<AtomicConstraint> atomicConstraints;
    private Collection<DecisionNode> decisions;
    private Set<NodeMarking> nodeMarkings;


    public ConstraintNodeBuilder(Collection<AtomicConstraint> atomicConstraints, Collection<DecisionNode> decisions, Set<NodeMarking> nodeMarkings) {
        this.atomicConstraints = atomicConstraints;
        this.decisions = decisions;
        this.nodeMarkings = nodeMarkings;
    }

    public ConstraintNodeBuilder() {
        this(Collections.emptyList(),Collections.emptyList(),Collections.emptySet());
    }


    public ConstraintNodeBuilder removeAtomicConstraint(AtomicConstraint atomicConstraint) {
        this.atomicConstraints = atomicConstraints
                .stream()
                .filter(constraint -> !constraint.equals(atomicConstraint))
                .collect(Collectors.toList());
        return this;
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

    public ConstraintNodeBuilder addAtomicConstraints(Collection<AtomicConstraint> constraints) {
        return new ConstraintNodeBuilder(
            concat(
                atomicConstraints.stream(),
                constraints.stream()
            ).collect(Collectors.toList()),
            decisions,
            nodeMarkings
        );
    }

    public ConstraintNodeBuilder addAtomicConstraints(AtomicConstraint... constraints) {
        return addAtomicConstraints(Arrays.asList(constraints));
    }

    public ConstraintNodeBuilder addDecision(DecisionNode decisionNode) {
        return setDecisions(
            concat(
                decisions.stream(),
                Stream.of(decisionNode)
            ).collect(Collectors.toList()));
    }

    public ConstraintNodeBuilder addDecisions(Collection<DecisionNode> newDecisions) {
        return setDecisions(
            concat(
                newDecisions.stream(),
                decisions.stream()
            ).collect(Collectors.toList()));
    }

    public ConstraintNodeBuilder setDecisions (Collection<DecisionNode> newDecisions) {
        return new ConstraintNodeBuilder(atomicConstraints, newDecisions, nodeMarkings);
    }

    public ConstraintNodeBuilder markNode(NodeMarking marking) {
        Set<NodeMarking> newMarkings = FlatMappingSpliterator.flatMap(
            Stream.of(Collections.singleton(marking), nodeMarkings),
            Collection::stream)
            .collect(Collectors.toSet());
        return new ConstraintNodeBuilder(atomicConstraints, decisions, newMarkings);
    }


    public ConstraintNode build() {
        return new ConstraintNode(atomicConstraints, decisions, nodeMarkings);
    }


    public ConstraintNodeBuilder setNodeMarkings(Set<NodeMarking> nodeMarkings) {
        this.nodeMarkings = nodeMarkings;
        return this;
    }


    public ConstraintNode createConstraintNode() {
        return new ConstraintNode(atomicConstraints, decisions, nodeMarkings);
    }
}
