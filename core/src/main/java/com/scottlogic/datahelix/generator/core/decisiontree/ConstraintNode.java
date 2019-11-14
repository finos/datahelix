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

import java.util.*;

public class ConstraintNode implements Node {
    private final Set<AtomicConstraint> atomicConstraints;
    private final Set<FieldSpecRelation> relations;
    private final Set<DecisionNode> decisions;
    private final Set<NodeMarking> nodeMarkings;

    public ConstraintNode(Set<AtomicConstraint> atomicConstraints,
                          Set<FieldSpecRelation> relations,
                          Set<DecisionNode> decisions,
                          Set<NodeMarking> nodeMarkings) {
        this.atomicConstraints = Collections.unmodifiableSet(atomicConstraints);
        this.relations = Collections.unmodifiableSet(relations);
        this.decisions = Collections.unmodifiableSet(decisions);
        this.nodeMarkings = Collections.unmodifiableSet(nodeMarkings);
    }

    public Set<AtomicConstraint> getAtomicConstraints() {
        return atomicConstraints;
    }


    public Set<FieldSpecRelation> getRelations() {
        return relations;
    }

    public Set<DecisionNode> getDecisions() {
        return decisions;
    }

    public String toString() {
        if (decisions.isEmpty())
            return atomicConstraints.size() > 5
                ? String.format("%d constraints", atomicConstraints.size())
                : Objects.toString(atomicConstraints);

        if (atomicConstraints.isEmpty())
            return decisions.size() > 5
                ? String.format("%d decisions", decisions.size())
                : Objects.toString(decisions);

        return String.format(
            "Decision: %s, Constraints: %s",
            decisions.size() > 5
                ? String.format("%d decisions", decisions.size())
                : Objects.toString(decisions),
            atomicConstraints.size() > 5
                ? String.format("%d constraints", atomicConstraints.size())
                : Objects.toString(atomicConstraints));
    }

    public ConstraintNodeBuilder builder() {
        return new ConstraintNodeBuilder(atomicConstraints, relations, decisions, nodeMarkings);
    }

    @Override
    public boolean hasMarking(NodeMarking detail) {
        return this.nodeMarkings.contains(detail);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConstraintNode that = (ConstraintNode) o;

        return Objects.equals(atomicConstraints, that.atomicConstraints) &&
            Objects.equals(relations, that.relations) &&
            Objects.equals(decisions, that.decisions) &&
            Objects.equals(nodeMarkings, that.nodeMarkings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(atomicConstraints, relations, decisions, nodeMarkings);
    }

    static ConstraintNode merge(Iterator<ConstraintNode> constraintNodeIterator) {
        Set<AtomicConstraint> atomicConstraints = new HashSet<>();
        Set<FieldSpecRelation> delayedAtomicConstraints = new HashSet<>();
        Set<DecisionNode> decisions = new HashSet<>();
        Set<NodeMarking> markings = new HashSet<>();

        while (constraintNodeIterator.hasNext()) {
            ConstraintNode constraintNode = constraintNodeIterator.next();

            atomicConstraints.addAll(constraintNode.getAtomicConstraints());
            delayedAtomicConstraints.addAll(constraintNode.getRelations());
            decisions.addAll(constraintNode.getDecisions());
            markings.addAll(constraintNode.nodeMarkings);
        }

        return new ConstraintNodeBuilder()
            .addAtomicConstraints(atomicConstraints)
            .addRelations(delayedAtomicConstraints)
            .setDecisions(decisions)
            .setNodeMarkings(markings)
            .build();
    }
}
