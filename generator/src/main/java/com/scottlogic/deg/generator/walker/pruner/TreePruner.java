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

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.decisiontree.*;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecHelper;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;

import java.util.*;

public class TreePruner {

    private final FieldSpecMerger merger;
    private final ConstraintReducer constraintReducer;
    private final FieldSpecHelper fieldSpecHelper;

    @Inject
    public TreePruner(FieldSpecMerger merger, ConstraintReducer constraintReducer, FieldSpecHelper fieldSpecHelper) {
        this.merger = merger;
        this.constraintReducer = constraintReducer;
        this.fieldSpecHelper = fieldSpecHelper;
    }

    /**
     * Prunes a tree of any branches that are contradictory to the value of the nextFixedField
     * @param constraintNode The Tree to be pruned
     * @param value the field and value to prune for.
     * @return A pruned tree if the new tree is valid, Merged.contradictory otherwise
     */
    public Merged<ConstraintNode> pruneConstraintNode(ConstraintNode constraintNode, Field field, DataBagValue value) {
        Map<Field, FieldSpec> fieldToSpec = new HashMap<>();
        fieldToSpec.put(field, fieldSpecHelper.getFieldSpecForValue(value));
        return pruneConstraintNode(constraintNode, fieldToSpec);
    }

    public Merged<ConstraintNode> pruneConstraintNode(ConstraintNode constraintNode, Map<Field, FieldSpec> fieldSpecs) {
        Merged<Map<Field, FieldSpec>> newFieldSpecs = combineConstraintsWithParent(constraintNode, fieldSpecs);
        if (newFieldSpecs.isContradictory()){
            return Merged.contradictory();
        }

        PrunedConstraintState state = new PrunedConstraintState(constraintNode);
        for (DecisionNode decision : constraintNode.getDecisions()) {

            Merged<DecisionNode> prunedDecisionNode = pruneDecisionNode(decision, newFieldSpecs.get());
            if (prunedDecisionNode.isContradictory()) {
                return Merged.contradictory();
            }

            state.addPrunedDecision(prunedDecisionNode.get());
        }

        if (state.hasPulledUpDecisions()){
            return pruneConstraintNode(
                state.getNewConstraintNode(),
                state.addPulledUpFieldsToMap(fieldSpecs));
        }

        return Merged.of(state.getNewConstraintNode());
    }

    private Merged<DecisionNode> pruneDecisionNode(DecisionNode decisionNode,  Map<Field, FieldSpec> fieldSpecs) {
        Set<ConstraintNode> newConstraintNodes = new HashSet<>();

        for (ConstraintNode constraintNode : decisionNode.getOptions()) {
            pruneConstraintNode(constraintNode, fieldSpecs).ifPresent(newConstraintNodes::add);
        }

        if (newConstraintNodes.isEmpty()) {
            return Merged.contradictory();
        }

        return Merged.of(new DecisionNode(newConstraintNodes));
    }

    private Merged<Map<Field, FieldSpec>> combineConstraintsWithParent(ConstraintNode constraintNode, Map<Field, FieldSpec> parentFieldSpecs) {
        Map<Field, Collection<AtomicConstraint>> relevantConstraints =
            getRelevantConstraints(constraintNode.getAtomicConstraints(), parentFieldSpecs.keySet());

        Merged<Map<Field, FieldSpec>> relevantFieldSpecs = createFieldSpecMap(relevantConstraints);
        if (relevantFieldSpecs.isContradictory()){
            return Merged.contradictory();
        }

        return mergeFieldSpecMaps(parentFieldSpecs, relevantFieldSpecs.get());
    }

    private Map<Field, Collection<AtomicConstraint>> getRelevantConstraints(Collection<AtomicConstraint> pulledUpAtomicConstraints, Set<Field> relevantFields) {
        Map<Field, Collection<AtomicConstraint>> map = new HashMap<>();
        pulledUpAtomicConstraints.stream()
            .filter(constraint -> relevantFields.contains(constraint.getField()))
            .forEach(constraint -> addToConstraintsMap(map, constraint));
        return map;
    }

    private Merged<Map<Field, FieldSpec>> createFieldSpecMap(Map<Field, Collection<AtomicConstraint>> relevantConstraints){
        Map<Field, FieldSpec> newFieldSpecs = new HashMap<>();
        for (Map.Entry<Field, Collection<AtomicConstraint>> fieldToConstraints : relevantConstraints.entrySet()) {

            Optional<FieldSpec> fieldSpec = constraintReducer.reduceConstraintsToFieldSpec(fieldToConstraints.getKey(), fieldToConstraints.getValue());
            if (!fieldSpec.isPresent()){
                return Merged.contradictory();
            }
            newFieldSpecs.put(fieldToConstraints.getKey(), fieldSpec.get());
        }

        return Merged.of(newFieldSpecs);
    }

    private Merged<Map<Field, FieldSpec>> mergeFieldSpecMaps(Map<Field, FieldSpec> parentFieldSpecs, Map<Field, FieldSpec> newMap) {
        for (Map.Entry<Field, FieldSpec> entry : parentFieldSpecs.entrySet()) {
            if (!newMap.containsKey(entry.getKey())){
                newMap.put(entry.getKey(), entry.getValue());
            }else {
                Optional<FieldSpec> mergedSpec = merger.merge(entry.getValue(), newMap.get(entry.getKey()));
                if (!mergedSpec.isPresent()){
                    return Merged.contradictory();
                }
                newMap.put(entry.getKey(), mergedSpec.get());
            }
        }

        return Merged.of(newMap);
    }

    private void addToConstraintsMap(Map<Field, Collection<AtomicConstraint>> map, AtomicConstraint constraint) {
        if (!map.containsKey(constraint.getField())) {
            map.put(constraint.getField(), new ArrayList<>());
        }

        map.get(constraint.getField())
            .add(constraint);
    }
}
