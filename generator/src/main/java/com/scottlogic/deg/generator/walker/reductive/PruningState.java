package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.decisiontree.TreeConstraintNode;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PruningState {

    Collection<AtomicConstraint> newAtomicConstraints;
    Collection<DecisionNode> newDecisionNodes = new ArrayList<>();
    Collection<AtomicConstraint> pulledUpAtomicConstraints = new ArrayList<>();

    public PruningState(ConstraintNode constraintNode){
        newAtomicConstraints = new ArrayList<>(constraintNode.getAtomicConstraints());
    }

    public void addPrunedDecision(DecisionNode prunedDecisionNode) {
        if (onlyOneOption(prunedDecisionNode)) {
            ConstraintNode remainingConstraintNode = getOnlyRemainingOption(prunedDecisionNode);

            pulledUpAtomicConstraints.addAll(remainingConstraintNode.getAtomicConstraints());
            newAtomicConstraints.addAll(remainingConstraintNode.getAtomicConstraints());
            newDecisionNodes.addAll(remainingConstraintNode.getDecisions());
        }
        else {
            newDecisionNodes.add(prunedDecisionNode);
        }
    }

    public boolean hasNoPulledUpDecisions() {
        return pulledUpAtomicConstraints.isEmpty();
    }

    public ConstraintNode getNewConstraintNode() {
        return new TreeConstraintNode(newAtomicConstraints, newDecisionNodes);
    }

    public Map<Field, FieldSpec> addPulledUpFieldsToMap(Map<Field, FieldSpec> previousFieldSpecs) {
        Map<Field, FieldSpec> mapWithPulledUpFields = new HashMap<>(previousFieldSpecs);
        for (AtomicConstraint c : pulledUpAtomicConstraints) {
            mapWithPulledUpFields.putIfAbsent(c.getField(), FieldSpec.Empty);
        }
        return mapWithPulledUpFields;
    }

    private boolean onlyOneOption(DecisionNode prunedDecisionNode) {
        return prunedDecisionNode.getOptions().size() == 1;
    }

    private ConstraintNode getOnlyRemainingOption(DecisionNode prunedDecisionNode) {
        return prunedDecisionNode.getOptions().iterator().next();
    }
}
