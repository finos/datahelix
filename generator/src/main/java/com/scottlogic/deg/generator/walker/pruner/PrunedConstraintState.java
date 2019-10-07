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

package com.scottlogic.deg.generator.walker.pruner;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.profile.constraints.delayed.DelayedAtomicConstraint;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.ConstraintNodeBuilder;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;

import java.util.*;

class PrunedConstraintState {

    private final Set<AtomicConstraint> newAtomicConstraints;
    private final Set<DelayedAtomicConstraint> newDelayedAtomicConstraints;
    private final Collection<DecisionNode> newDecisionNodes = new ArrayList<>();
    private final Set<AtomicConstraint> pulledUpAtomicConstraints = new HashSet<>();
    private final Set<DelayedAtomicConstraint> pulledUpDelayedAtomicConstraints = new HashSet<>();

    PrunedConstraintState(ConstraintNode constraintNode){
        newAtomicConstraints = new HashSet<>(constraintNode.getAtomicConstraints());
        newDelayedAtomicConstraints = new HashSet<>(constraintNode.getDelayedAtomicConstraints());
    }

    void addPrunedDecision(DecisionNode prunedDecisionNode) {
        if (!onlyOneOption(prunedDecisionNode)) {
            newDecisionNodes.add(prunedDecisionNode);
            return;
        }
        
        ConstraintNode remainingConstraintNode = getOnlyRemainingOption(prunedDecisionNode);
        pulledUpAtomicConstraints.addAll(remainingConstraintNode.getAtomicConstraints());
        pulledUpDelayedAtomicConstraints.addAll(remainingConstraintNode.getDelayedAtomicConstraints());
        newAtomicConstraints.addAll(remainingConstraintNode.getAtomicConstraints());
        newDelayedAtomicConstraints.addAll(remainingConstraintNode.getDelayedAtomicConstraints());
        newDecisionNodes.addAll(remainingConstraintNode.getDecisions());
    }

    boolean hasPulledUpDecisions() {
        return !(pulledUpAtomicConstraints.isEmpty() || pulledUpDelayedAtomicConstraints.isEmpty());
    }

    ConstraintNode getNewConstraintNode() {
        return new ConstraintNodeBuilder()
            .addAtomicConstraints(newAtomicConstraints)
            .addDelayedAtomicConstraints(newDelayedAtomicConstraints)
            .setDecisions(newDecisionNodes)
            .build();
    }

    Map<Field, FieldSpec> addPulledUpFieldsToMap(Map<Field, FieldSpec> previousFieldSpecs) {
        Map<Field, FieldSpec> mapWithPulledUpFields = new HashMap<>(previousFieldSpecs);
        for (AtomicConstraint c : pulledUpAtomicConstraints) {
            mapWithPulledUpFields.putIfAbsent(c.getField(), FieldSpec.empty());
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
