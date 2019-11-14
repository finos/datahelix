/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.scottlogic.datahelix.generator.core.decisiontree;

import com.scottlogic.datahelix.generator.core.fieldspecs.relations.FieldSpecRelation;
import com.scottlogic.datahelix.generator.core.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.datahelix.generator.common.util.FlatMappingSpliterator;
import com.scottlogic.datahelix.generator.core.utils.SetUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Stream.concat;

public class ConstraintNodeBuilder {
    private final Set<AtomicConstraint> atomicConstraints;
    private final Set<FieldSpecRelation> relations;
    private final Set<DecisionNode> decisions;
    private final Set<NodeMarking> nodeMarkings;

    public ConstraintNodeBuilder(Set<AtomicConstraint> atomicConstraints,
                                 Set<FieldSpecRelation> relations,
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

    public ConstraintNodeBuilder setRelations(Set<FieldSpecRelation> newConstraints) {
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

    public ConstraintNodeBuilder addRelations(Set<FieldSpecRelation> relations) {
        return setRelations(
            concat(
                this.relations.stream(),
                relations.stream())
            .collect(Collectors.toSet())
        );
    }

    public ConstraintNodeBuilder addRelations(FieldSpecRelation... constraints) {
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
