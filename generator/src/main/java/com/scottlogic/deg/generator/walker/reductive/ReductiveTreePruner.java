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

public class ReductiveTreePruner {

    private final FieldSpecMerger merger;
    private final ConstraintReducer constraintReducer;

    @Inject
    public ReductiveTreePruner(FieldSpecMerger merger, ConstraintReducer constraintReducer) {
        this.merger = merger;
        this.constraintReducer = constraintReducer;
    }

    /**
     * Prunes a tree of any branches that are contradictory to the value of the nextFixedField
     * @param constraintNode The Tree to be pruned
     * @param nextFixedField The field, and the value to fix it for.
     * @return A pruned tree if the new tree is valid, Combined.contradictory otherwise
     */
    public Combined<ConstraintNode> pruneConstraintNode(ConstraintNode constraintNode, FixedField nextFixedField) {
        return pruneConstraintNode(constraintNode, nextFixedField.getField(), nextFixedField.getFieldSpecForCurrentValue());
    }

    private Combined<ConstraintNode> pruneConstraintNode(ConstraintNode constraintNode, Field field, FieldSpec mergingFieldSpec) {

        Combined<FieldSpec> newFieldSpec = combineConstraintsWithParent(field, constraintNode, mergingFieldSpec);
        if (newFieldSpec.isContradictory()){
            return Combined.contradictory();
        }

        Collection<AtomicConstraint> newAtomicConstraints = new ArrayList<>(constraintNode.getAtomicConstraints());
        Collection<DecisionNode> newDecisionNodes = new ArrayList<>();

        for (DecisionNode decision : constraintNode.getDecisions()) {
            Combined<DecisionNode> prunedDecisionNode = pruneDecisionNode(decision, field, newFieldSpec.get());

            if (prunedDecisionNode.isContradictory()) {
                return Combined.contradictory();
            }

            if (onlyOneOption(prunedDecisionNode)) {
                ConstraintNode remainingConstraintNode = getOnlyRemainingOption(prunedDecisionNode);

                newAtomicConstraints.addAll(remainingConstraintNode.getAtomicConstraints());
                newDecisionNodes.addAll(remainingConstraintNode.getDecisions());
            }
            else {
                newDecisionNodes.add(prunedDecisionNode.get());
            }
        }

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

    private Combined<FieldSpec> combineConstraintsWithParent(Field field, ConstraintNode constraintNode, FieldSpec parentFieldSpec){
        List<AtomicConstraint> atomicConstraintsForField =
            AtomicConstraintsHelper.getConstraintsForField(constraintNode.getAtomicConstraints(), field);

        Optional<FieldSpec> nodeFieldSpec = constraintReducer.reduceConstraintsToFieldSpec(atomicConstraintsForField);

        if (!nodeFieldSpec.isPresent()) {
            return Combined.contradictory();
        }

        Optional<FieldSpec> newFieldSpec = merger.merge(nodeFieldSpec.get(), parentFieldSpec);

        if (!newFieldSpec.isPresent()) {
            return Combined.contradictory();
        }

        return Combined.of(newFieldSpec.get());
    }

    private boolean onlyOneOption(Combined<DecisionNode> prunedDecisionNode) {
        return prunedDecisionNode.get().getOptions().size() == 1;
    }

    private ConstraintNode getOnlyRemainingOption(Combined<DecisionNode> prunedDecisionNode) {
        return prunedDecisionNode.get().getOptions().iterator().next();
    }
}
