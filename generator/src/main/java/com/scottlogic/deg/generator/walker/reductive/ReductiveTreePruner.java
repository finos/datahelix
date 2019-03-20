package com.scottlogic.deg.generator.walker.reductive;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraintsHelper;
import com.scottlogic.deg.generator.decisiontree.*;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecHelper;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy.FieldValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ReductiveTreePruner {

    private final FieldSpecMerger merger;
    private final ConstraintReducer constraintReducer;
    private final FieldSpecHelper fieldSpecHelper;

    @Inject
    public ReductiveTreePruner(FieldSpecMerger merger, ConstraintReducer constraintReducer, FieldSpecHelper fieldSpecHelper) {
        this.merger = merger;
        this.constraintReducer = constraintReducer;
        this.fieldSpecHelper = fieldSpecHelper;
    }

    /**
     * Prunes a tree of any branches that are contradictory to the value of the nextFixedField
     * @param constraintNode The Tree to be pruned
     * @param value the field and value to prune for.
     * @return A pruned tree if the new tree is valid, Combined.contradictory otherwise
     */
    public Merged<ConstraintNode> pruneConstraintNode(ConstraintNode constraintNode, FieldValue value) {
        return pruneConstraintNode(constraintNode, value.getField(), fieldSpecHelper.getFieldSpecForValue(value.getValue()));
    }

    private Merged<ConstraintNode> pruneConstraintNode(ConstraintNode constraintNode, Field field, FieldSpec mergingFieldSpec) {
        Merged<FieldSpec> newFieldSpec = combineConstraintsWithParent(field, constraintNode, mergingFieldSpec);
        if (newFieldSpec.isContradictory()){
            return Merged.contradictory();
        }

        Collection<AtomicConstraint> newAtomicConstraints = new ArrayList<>(constraintNode.getAtomicConstraints());
        Collection<DecisionNode> newDecisionNodes = new ArrayList<>();

        for (DecisionNode decision : constraintNode.getDecisions()) {
            Merged<DecisionNode> prunedDecisionNode = pruneDecisionNode(decision, field, newFieldSpec.get());

            if (prunedDecisionNode.isContradictory()) {
                return Merged.contradictory();
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

        return Merged.of(new TreeConstraintNode(newAtomicConstraints, newDecisionNodes));
    }

    private Merged<DecisionNode> pruneDecisionNode(DecisionNode decisionNode, Field field, FieldSpec mergingFieldSpec) {
        Collection<ConstraintNode> newConstraintNodes = new ArrayList<>();

        for (ConstraintNode constraintNode : decisionNode.getOptions()) {
            pruneConstraintNode(constraintNode, field, mergingFieldSpec).ifPresent(newConstraintNodes::add);
        }

        if (newConstraintNodes.isEmpty()) {
            return Merged.contradictory();
        }

        return Merged.of(new TreeDecisionNode(newConstraintNodes));
    }

    private Merged<FieldSpec> combineConstraintsWithParent(Field field, ConstraintNode constraintNode, FieldSpec parentFieldSpec){
        List<AtomicConstraint> atomicConstraintsForField =
            AtomicConstraintsHelper.getConstraintsForField(constraintNode.getAtomicConstraints(), field);

        Optional<FieldSpec> nodeFieldSpec = constraintReducer.reduceConstraintsToFieldSpec(atomicConstraintsForField);

        if (!nodeFieldSpec.isPresent()) {
            return Merged.contradictory();
        }

        Optional<FieldSpec> newFieldSpec = merger.merge(nodeFieldSpec.get(), parentFieldSpec);

        if (!newFieldSpec.isPresent()) {
            return Merged.contradictory();
        }

        return Merged.of(newFieldSpec.get());
    }

    private boolean onlyOneOption(Merged<DecisionNode> prunedDecisionNode) {
        return prunedDecisionNode.get().getOptions().size() == 1;
    }

    private ConstraintNode getOnlyRemainingOption(Merged<DecisionNode> prunedDecisionNode) {
        return prunedDecisionNode.get().getOptions().iterator().next();
    }
}
