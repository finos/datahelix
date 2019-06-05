package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.common.util.FlatMappingSpliterator;
import com.scottlogic.deg.common.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class TreeConstraintNode implements ConstraintNode {
    public static final ConstraintNode empty = new TreeConstraintNode(Collections.emptySet(), Collections.emptySet(), Collections.emptySet());

    private final Collection<AtomicConstraint> atomicConstraints;
    private final Collection<DecisionNode> decisions;
    private final Set<NodeMarking> nodeMarkings;

    public TreeConstraintNode(Collection<AtomicConstraint> atomicConstraints, Collection<DecisionNode> decisions) {
        this(atomicConstraints, decisions, Collections.emptySet());
    }

    public TreeConstraintNode(Collection<AtomicConstraint> atomicConstraints, Collection<DecisionNode> decisions, Set<NodeMarking> nodeMarkings) {
        this.atomicConstraints = Collections.unmodifiableCollection(atomicConstraints);
        this.decisions = Collections.unmodifiableCollection(decisions);
        this.nodeMarkings = Collections.unmodifiableSet(nodeMarkings);
    }

    public TreeConstraintNode(AtomicConstraint... atomicConstraints) {
        this(
            Arrays.asList(atomicConstraints),
            Collections.emptySet(),
            Collections.emptySet());
    }

    public TreeConstraintNode(AtomicConstraint singleAtomicConstraint) {
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

        return new TreeConstraintNode(
          this.atomicConstraints,
          decisions.stream()
              .filter(existingDecision -> !shouldRemove.apply(existingDecision))
              .collect(Collectors.toList()),
            this.nodeMarkings
        );
    }

    public ConstraintNode cloneWithoutAtomicConstraint(AtomicConstraint excludeAtomicConstraint) {
        return new TreeConstraintNode(
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
        return new TreeConstraintNode(
            Stream
                .concat(
                    this.atomicConstraints.stream(),
                    constraints.stream())
                .collect(Collectors.toList()),
            this.decisions,
            this.nodeMarkings
        );
    }

    @Override
    public ConstraintNode addDecisions(Collection<DecisionNode> decisions) {
        return new TreeConstraintNode(
            atomicConstraints,
            Stream
                .concat(
                    this.decisions.stream(),
                    decisions.stream())
                .collect(Collectors.toList()),
            this.nodeMarkings
        );
    }

    @Override
    public ConstraintNode setDecisions(Collection<DecisionNode> decisions) {
        return new TreeConstraintNode(this.atomicConstraints, decisions, this.nodeMarkings);
    }

    @Override
    public boolean hasMarking(NodeMarking detail) {
        return this.nodeMarkings.contains(detail);
    }

    @Override
    public ConstraintNode markNode(NodeMarking marking) {
        Set<NodeMarking> newMarkings = FlatMappingSpliterator.flatMap(
            Stream.of(Collections.singleton(marking), this.nodeMarkings),
            Collection::stream)
            .collect(Collectors.toSet());
        return new TreeConstraintNode(this.atomicConstraints, this.decisions, newMarkings);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TreeConstraintNode that = (TreeConstraintNode) o;
        return Objects.equals(atomicConstraints, that.atomicConstraints) &&
            Objects.equals(decisions, that.decisions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(atomicConstraints, decisions);
    }

    @Override
    public ConstraintNode accept(NodeVisitor visitor){
        Stream<DecisionNode> decisionNodeStream = getDecisions().stream().map(d -> d.accept(visitor));

        return visitor.visit(
            new TreeConstraintNode(
                new ArrayList<>(atomicConstraints),
                decisionNodeStream.collect(Collectors.toSet()),
                nodeMarkings));
    }
}
