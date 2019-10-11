package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.fieldspecs.relations.FieldSpecRelations;
import com.scottlogic.deg.generator.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.common.util.FlatMappingSpliterator;
import com.scottlogic.deg.generator.utils.SetUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Stream.concat;

public class ConstraintNodeBuilder {
    private final Set<AtomicConstraint> atomicConstraints;
    private final Set<FieldSpecRelations> relations;
    private final Set<DecisionNode> decisions;
    private final Set<NodeMarking> nodeMarkings;

    public ConstraintNodeBuilder(Set<AtomicConstraint> atomicConstraints,
                                 Set<FieldSpecRelations> relations,
                                 Set<DecisionNode> decisions,
                                 Set<NodeMarking> nodeMarkings) {
        this.atomicConstraints = atomicConstraints;
        this.relations = relations;
        this.decisions = decisions;
        this.nodeMarkings = nodeMarkings;
    }

    public ConstraintNodeBuilder() {
        this(Collections.emptySet(), Collections.emptySet(), Collections.emptySet(), Collections.emptySet());
    }

    public ConstraintNodeBuilder setAtomicConstraints(Set<AtomicConstraint> newAtomicConstraints) {
        return new ConstraintNodeBuilder(newAtomicConstraints, relations, decisions, nodeMarkings);
    }

    public ConstraintNodeBuilder setRelations(Set<FieldSpecRelations> newConstraints) {
        return new ConstraintNodeBuilder(atomicConstraints, newConstraints, decisions, nodeMarkings);
    }

    public ConstraintNodeBuilder removeAtomicConstraint(AtomicConstraint atomicConstraint) {
        return setAtomicConstraints(atomicConstraints
            .stream()
            .filter(constraint -> !constraint.equals(atomicConstraint))
            .collect(Collectors.toSet())
        );
    }

    public ConstraintNodeBuilder addAtomicConstraints(Set<AtomicConstraint> atomicConstraints) {
        return setAtomicConstraints(
            concat(
                this.atomicConstraints.stream(),
                atomicConstraints.stream()
            ).collect(Collectors.toSet())
        );
    }

    public ConstraintNodeBuilder addAtomicConstraints(AtomicConstraint... constraints) {
        return addAtomicConstraints(Arrays.stream(constraints).collect(Collectors.toSet()));
    }

    public ConstraintNodeBuilder addRelations(Set<FieldSpecRelations> relations) {
        return setRelations(
            concat(
                this.relations.stream(),
                relations.stream())
            .collect(Collectors.toSet())
        );
    }

    public ConstraintNodeBuilder addRelations(FieldSpecRelations... constraints) {
        return addRelations(SetUtils.setOf(constraints));
    }

    public ConstraintNodeBuilder setDecisions(Set<DecisionNode> newDecisions) {
        return new ConstraintNodeBuilder(atomicConstraints, relations, newDecisions, nodeMarkings);
    }

    public ConstraintNodeBuilder removeDecision(DecisionNode decisionNode) {
        return removeDecisions(Collections.singleton(decisionNode));
    }

    public ConstraintNodeBuilder removeDecisions(Collection<DecisionNode> decisionNodes) {
        return setDecisions(
            decisions.stream()
                .filter(decision -> !decisionNodes.contains(decision))
                .collect(Collectors.toSet()));
    }

    public ConstraintNodeBuilder addDecision(DecisionNode decisionNode) {
        return addDecisions(Collections.singleton(decisionNode));
    }

    public ConstraintNodeBuilder addDecisions(Collection<DecisionNode> decisions) {
        return setDecisions(
            concat(
                this.decisions.stream(),
                decisions.stream()
            ).collect(Collectors.toSet()));
    }


    public ConstraintNodeBuilder setNodeMarkings(Set<NodeMarking> newNodeMarkings) {
        return new ConstraintNodeBuilder(atomicConstraints, relations, decisions, newNodeMarkings);
    }

    public ConstraintNodeBuilder markNode(NodeMarking marking) {
        return setNodeMarkings(
            FlatMappingSpliterator.flatMap(
                Stream.of(Collections.singleton(marking), nodeMarkings),
                Collection::stream
            ).collect(Collectors.toSet()));
    }

    public ConstraintNode build() {
        return new ConstraintNode(atomicConstraints, relations, decisions, nodeMarkings);
    }

}
