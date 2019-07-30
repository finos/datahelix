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

package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.ConstraintNodeBuilder;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class PrunedConstraintState {

    private final Collection<AtomicConstraint> newAtomicConstraints;
    private final Collection<DecisionNode> newDecisionNodes = new ArrayList<>();
    private final Collection<AtomicConstraint> pulledUpAtomicConstraints = new ArrayList<>();

    PrunedConstraintState(ConstraintNode constraintNode){
        newAtomicConstraints = new ArrayList<>(constraintNode.getAtomicConstraints());
    }

    void addPrunedDecision(DecisionNode prunedDecisionNode) {
        if (!onlyOneOption(prunedDecisionNode)) {
            newDecisionNodes.add(prunedDecisionNode);
            return;
        }
        
        ConstraintNode remainingConstraintNode = getOnlyRemainingOption(prunedDecisionNode);
        pulledUpAtomicConstraints.addAll(remainingConstraintNode.getAtomicConstraints());
        newAtomicConstraints.addAll(remainingConstraintNode.getAtomicConstraints());
        newDecisionNodes.addAll(remainingConstraintNode.getDecisions());
    }

    boolean hasPulledUpDecisions() {
        return !pulledUpAtomicConstraints.isEmpty();
    }

    ConstraintNode getNewConstraintNode() {
        return new ConstraintNodeBuilder().addAtomicConstraints(newAtomicConstraints).setDecisions(newDecisionNodes).createConstraintNode();
    }

    Map<Field, FieldSpec> addPulledUpFieldsToMap(Map<Field, FieldSpec> previousFieldSpecs) {
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
