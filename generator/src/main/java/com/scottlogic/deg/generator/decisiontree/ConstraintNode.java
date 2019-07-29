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

package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.common.util.FlatMappingSpliterator;
import com.scottlogic.deg.common.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConstraintNode implements Node {
    private final Collection<AtomicConstraint> atomicConstraints;
    private final Collection<DecisionNode> decisions;
    private final Set<NodeMarking> nodeMarkings;

    public ConstraintNode(Collection<AtomicConstraint> atomicConstraints, Collection<DecisionNode> decisions) {
        this(atomicConstraints, decisions, Collections.emptySet());
    }

    public ConstraintNode(Collection<AtomicConstraint> atomicConstraints, Collection<DecisionNode> decisions, Set<NodeMarking> nodeMarkings) {
        this.atomicConstraints = Collections.unmodifiableCollection(atomicConstraints);
        this.decisions = Collections.unmodifiableCollection(decisions);
        this.nodeMarkings = Collections.unmodifiableSet(nodeMarkings);
    }

    public ConstraintNode(AtomicConstraint... atomicConstraints) {
        this(
            Arrays.asList(atomicConstraints),
            Collections.emptySet(),
            Collections.emptySet());
    }

    public ConstraintNode(AtomicConstraint singleAtomicConstraint) {
        this(
            Collections.singletonList(singleAtomicConstraint),
            Collections.emptySet(),
            Collections.emptySet());
    }

    public Collection<AtomicConstraint> getAtomicConstraints() {
        return new HashSet<>(atomicConstraints);
    }

    public Collection<DecisionNode> getDecisions() {
        return decisions;
    }

    public Optional<RowSpec> getOrCreateRowSpec(Supplier<Optional<RowSpec>> createRowSpecFunc) {
        if (adaptedRowSpec != null)
            return adaptedRowSpec;

        adaptedRowSpec = createRowSpecFunc.get();
        return adaptedRowSpec;
    }
    private Optional<RowSpec> adaptedRowSpec = null;

    public String toString(){
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

    public ConstraintNode removeDecisions(Collection<DecisionNode> decisionsToRemove) {
        Function<DecisionNode, Boolean> shouldRemove = existingDecision -> decisionsToRemove.stream()
            .anyMatch(decisionToExclude -> decisionToExclude.equals(existingDecision));

        return new ConstraintNode(
          this.atomicConstraints,
          decisions.stream()
              .filter(existingDecision -> !shouldRemove.apply(existingDecision))
              .collect(Collectors.toList()),
            this.nodeMarkings
        );
    }

    public ConstraintNode cloneWithoutAtomicConstraint(AtomicConstraint excludeAtomicConstraint) {
        return new ConstraintNode(
            this.atomicConstraints
                .stream()
                .filter(c -> !c.equals(excludeAtomicConstraint))
                .collect(Collectors.toList()),
            decisions,
            this.nodeMarkings);
    }

    public boolean atomicConstraintExists(AtomicConstraint constraint) {
        return atomicConstraints
            .stream()
            .anyMatch(c -> c.equals(constraint));
    }

    public ConstraintNode addAtomicConstraints(Collection<AtomicConstraint> constraints) {
        return new ConstraintNode(
            Stream
                .concat(
                    this.atomicConstraints.stream(),
                    constraints.stream())
                .collect(Collectors.toList()),
            this.decisions,
            this.nodeMarkings
        );
    }

    public ConstraintNode addDecisions(Collection<DecisionNode> decisions) {
        return new ConstraintNode(
            atomicConstraints,
            Stream
                .concat(
                    this.decisions.stream(),
                    decisions.stream())
                .collect(Collectors.toList()),
            this.nodeMarkings
        );
    }

    public ConstraintNode setDecisions(Collection<DecisionNode> decisions) {
        return new ConstraintNode(this.atomicConstraints, decisions, this.nodeMarkings);
    }

    @Override
    public boolean hasMarking(NodeMarking detail) {
        return this.nodeMarkings.contains(detail);
    }

    public ConstraintNode markNode(NodeMarking marking) {
        Set<NodeMarking> newMarkings = FlatMappingSpliterator.flatMap(
            Stream.of(Collections.singleton(marking), this.nodeMarkings),
            Collection::stream)
            .collect(Collectors.toSet());
        return new ConstraintNode(this.atomicConstraints, this.decisions, newMarkings);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConstraintNode that = (ConstraintNode) o;
        return Objects.equals(atomicConstraints, that.atomicConstraints) &&
            Objects.equals(decisions, that.decisions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(atomicConstraints, decisions);
    }


    public ConstraintNode accept(NodeVisitor visitor){
        Stream<DecisionNode> decisionNodeStream = getDecisions().stream().map(d -> d.accept(visitor));

        return visitor.visit(
            new ConstraintNode(
                new ArrayList<>(atomicConstraints),
                decisionNodeStream.collect(Collectors.toSet()),
                nodeMarkings));
    }

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

        return new ConstraintNode(atomicConstraints, decisions, markings);
    }

     public Set<NodeMarking> getNodeMarkings(){
        return Arrays.stream(NodeMarking.values())
            .filter(this::hasMarking)
            .collect(Collectors.toSet());
    }
}
