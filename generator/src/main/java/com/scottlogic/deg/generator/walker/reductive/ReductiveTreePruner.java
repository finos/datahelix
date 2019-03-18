package com.scottlogic.deg.generator.walker.reductive;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraintsHelper;
import com.scottlogic.deg.generator.decisiontree.*;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReductiveTreePruner {

    private final FieldSpecMerger merger;
    private final ConstraintReducer constraintReducer;

    @Inject
    public ReductiveTreePruner(FieldSpecMerger merger, ConstraintReducer constraintReducer) {
        this.merger = merger;
        this.constraintReducer = constraintReducer;
    }

    public Optional<ConstraintNode> pruneConstraintNode(ConstraintNode constraintNode, FixedField lastFixedField) {
        Combined<ConstraintNode> newConstraintNode = pruneConstraintNode(constraintNode, lastFixedField.getField(), lastFixedField.getFieldSpecForCurrentValue());

        if (newConstraintNode.isContradictory()){
            return Optional.empty();
        }

        if (isContradictory(newConstraintNode.get())){
            return Optional.empty();
        }

        return Optional.of(newConstraintNode.get());
    }

    private Combined<ConstraintNode> pruneConstraintNode(ConstraintNode constraintNode, Field field, FieldSpec mergingFieldSpec) {
        // Get field spec from current constraintNode
        List<AtomicConstraint> atomicConstraintsForField =
            AtomicConstraintsHelper.getConstraintsForField(constraintNode.getAtomicConstraints(), field);

        Optional<FieldSpec> nodeFieldSpec = constraintReducer.reduceConstraintsToFieldSpec(atomicConstraintsForField);

        if (!nodeFieldSpec.isPresent()) {
            return Combined.contradictory();
        }

        // Merge with spec from parent
        Optional<FieldSpec> newFieldSpec = merger.merge(nodeFieldSpec.get(), mergingFieldSpec);

        // If contradictory -> return Optional.empty
        if (!newFieldSpec.isPresent()) {
            return Combined.contradictory();
        }

        Collection<DecisionNode> newDecisionNodes = new ArrayList<>();
        Collection<AtomicConstraint> newAtomicConstraints = new ArrayList<>();
        newAtomicConstraints.addAll(constraintNode.getAtomicConstraints());

        // Foreach decision below the constraint node, run pruneDecisionNode()
        for (DecisionNode decision : constraintNode.getDecisions()) {
            Combined<DecisionNode> prunedDecisionNode = pruneDecisionNode(decision, field, newFieldSpec.get());

            if (prunedDecisionNode.isContradictory()) {
                return Combined.contradictory();
            }

            if (prunedDecisionNode.get().getOptions().size() == 1) {
                ConstraintNode remainingConstraintNode = prunedDecisionNode.get().getOptions().iterator().next();
                newAtomicConstraints.addAll(remainingConstraintNode.getAtomicConstraints());
                newDecisionNodes.addAll(remainingConstraintNode.getDecisions());
            }
            else {
                newDecisionNodes.add(prunedDecisionNode.get());
            }
        }

        // Return new Constraint node
        return Combined.of(new TreeConstraintNode(newAtomicConstraints, newDecisionNodes));
    }

    private Combined<DecisionNode> pruneDecisionNode(DecisionNode decisionNode, Field field, FieldSpec mergingFieldSpec) {
        Collection<ConstraintNode> newConstraintNodes = new ArrayList<>();

        for (ConstraintNode constraintNode : decisionNode.getOptions()) {
            pruneConstraintNode(constraintNode, field, mergingFieldSpec).ifPresent(newConstraintNodes::add);
        }

        if (newConstraintNodes.isEmpty()) {
            return Combined.contradictory();
        }

        return Combined.of(new TreeDecisionNode(newConstraintNodes));
    }

    private boolean isContradictory(ConstraintNode constraintNode) {
        List<Field> constraintFields = constraintNode.getAtomicConstraints().stream()
            .map(AtomicConstraint::getField)
            .distinct()
            .collect(Collectors.toList());

        for (Field field : constraintFields) {
            List<AtomicConstraint> constraintsForField = AtomicConstraintsHelper.getConstraintsForField(constraintNode.getAtomicConstraints(), field);
            Optional<FieldSpec> fieldSpec = constraintReducer.reduceConstraintsToFieldSpec(constraintsForField);
            if (!fieldSpec.isPresent()){
                return true;
            }
        }
        return false;
    }
}
