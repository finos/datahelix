package com.scottlogic.deg.generator.walker.reductive;

import com.google.inject.Inject;
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

public class ReductiveTreePruner {

    private final FieldSpecMerger merger;
    private final ConstraintReducer constraintReducer;

    @Inject
    public ReductiveTreePruner(FieldSpecMerger merger, ConstraintReducer constraintReducer) {
        this.merger = merger;
        this.constraintReducer = constraintReducer;
    }

    public Optional<DecisionNode> pruneDecisionNode(DecisionNode decisionNode, FixedField lastFixedField) {
        Collection<ConstraintNode> newConstraintNodes = new ArrayList<>();

        for (ConstraintNode constraintNode : decisionNode.getOptions()) {
            pruneConstraintNode(constraintNode, lastFixedField).ifPresent(newConstraintNodes::add);
        }

        if (newConstraintNodes.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new TreeDecisionNode(newConstraintNodes));

    }

    public Optional<ConstraintNode> pruneConstraintNode(ConstraintNode constraintNode, FixedField lastFixedField) {
        // Get field spec from current constraintNode
        List<AtomicConstraint> atomicConstraintsForField =
            AtomicConstraintsHelper.getConstraintsForField(constraintNode.getAtomicConstraints(), lastFixedField.getField());

        Optional<FieldSpec> nodeFieldSpec = constraintReducer.reduceConstraintsToFieldSpec(atomicConstraintsForField);

        // Merge with spec from parent
        Optional<FieldSpec> newFieldSpec = merger.merge(nodeFieldSpec.get(), lastFixedField.getFieldSpecForValues());

        // If contradictory -> return Optional.empty?
        if (!newFieldSpec.isPresent()) {
            return Optional.empty();
        }

        Collection<DecisionNode> newDecisionNodes = new ArrayList<>();
        Collection<AtomicConstraint> newAtomicConstraints = new ArrayList<>();
        newAtomicConstraints.addAll(constraintNode.getAtomicConstraints());

        // Foreach decision below the constraint node, run pruneDecisionNode()
        for (DecisionNode decision : constraintNode.getDecisions()) {
            Optional<DecisionNode> decisionNode = pruneDecisionNode(decision, lastFixedField);

            if (!decisionNode.isPresent()) {
                return Optional.empty();
            }

            if (decisionNode.get().getOptions().size() == 1) {
                 newAtomicConstraints.addAll(decisionNode.get().getOptions().iterator().next().getAtomicConstraints());
            } else {
                newDecisionNodes.add(decisionNode.get());
            }
        }

        // Return new Constraint node
        return Optional.of(new TreeConstraintNode(newAtomicConstraints, newDecisionNodes));
    }
}
