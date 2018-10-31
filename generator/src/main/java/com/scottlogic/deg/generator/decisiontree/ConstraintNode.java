package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.restrictions.RowSpec;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class ConstraintNode {
    public static ConstraintNode merge(Iterator<ConstraintNode> constraintNodeIterator) {
        Collection<IConstraint> atomicConstraints = new ArrayList<>();
        Collection<DecisionNode> decisions = new ArrayList<>();

        while (constraintNodeIterator.hasNext()) {
            ConstraintNode constraintNode = constraintNodeIterator.next();

            atomicConstraints.addAll(constraintNode.atomicConstraints);
            decisions.addAll(constraintNode.decisions);
        }

        return new ConstraintNode(atomicConstraints, decisions);
    }

    private final Collection<IConstraint> atomicConstraints;
    private final Collection<DecisionNode> decisions;

    public ConstraintNode(Collection<IConstraint> atomicConstraints, Collection<DecisionNode> decisions) {
        this.atomicConstraints =  new ArrayList<>(atomicConstraints);
        this.decisions = new ArrayList<>(decisions);
    }

    public ConstraintNode(IConstraint... atomicConstraints) {
        this(
            Arrays.asList(atomicConstraints),
            new ArrayList<>());
    }

    public ConstraintNode(IConstraint singleAtomicConstraint) {
        decisions = new ArrayList<>();
        atomicConstraints = new ArrayList<>();
        atomicConstraints.add(singleAtomicConstraint);
    }

    ConstraintNode(DecisionNode... decisionNodes) {
        this(
            Collections.emptyList(),
            Arrays.asList(decisionNodes));
    }

    public Collection<IConstraint> getAtomicConstraints() {
        return new ArrayList<>(atomicConstraints);
    }

    public Collection<DecisionNode> getDecisions() {
        return new ArrayList<>(decisions);
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

    public void addDecision(DecisionNode decision){
        decisions.add(decision);
    }

    public DecisionNode addDecision() {
        DecisionNode newDecision = new DecisionNode();
        decisions.add(newDecision);
        return newDecision;
    }

    public void removeDecision(DecisionNode decision) {
        decisions.remove(decision);
    }

    public ConstraintNode cloneWithoutAtomicConstraint(IConstraint excludeAtomicConstraint) {
        return new ConstraintNode(
                this.atomicConstraints
                        .stream()
                        .filter(c -> !c.equals(excludeAtomicConstraint))
                        .collect(Collectors.toList()),
                decisions);
    }

    public boolean atomicConstraintExists(ConstraintNode constraintNode) {
        return constraintNode.atomicConstraints
                .stream()
                .anyMatch(ac -> atomicConstraintExists(ac));
    }

    public boolean atomicConstraintExists(IConstraint constraint) {
        return atomicConstraints
                .stream()
                .anyMatch(c -> c.equals(constraint));
    }

    public void addAtomicConstraints(Collection<IConstraint> constraints) {
        this.atomicConstraints.addAll(constraints);
    }
}
