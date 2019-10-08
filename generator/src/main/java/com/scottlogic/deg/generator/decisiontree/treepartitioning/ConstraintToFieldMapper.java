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

package com.scottlogic.deg.generator.decisiontree.treepartitioning;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.util.FlatMappingSpliterator;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.utils.SetUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Given a decision tree, find which constraints and decisions act on which fields and return a map from them to fields
 */
class ConstraintToFieldMapper {

    private class ConstraintToFields {
        public RootLevelConstraint constraint;
        public Set<Field> fields;

        ConstraintToFields(RootLevelConstraint constraint, Set<Field> fields) {
            this.constraint = constraint;
            this.fields = fields;
        }

        ConstraintToFields(RootLevelConstraint constraint, Field field) {
            this.constraint = constraint;
            this.fields = Collections.singleton(field);
        }
    }

    private Stream<ConstraintToFields> mapConstraintToFields(ConstraintNode node) {
        return Stream.of(
            mapAtomicConstraintsToFields(node),
            mapDelayedConstraintsToFields(node),
            mapDecisionsToFields(node))
            .reduce(Stream::concat)
            .orElseGet(Stream::empty);
    }

    private Stream<ConstraintToFields> mapAtomicConstraintsToFields(ConstraintNode node) {
        return node.getAtomicConstraints().stream()
            .map(constraint -> new ConstraintToFields(new RootLevelConstraint(constraint), constraint.getField()));
    }

    private Stream<ConstraintToFields> mapDelayedConstraintsToFields(ConstraintNode node) {
        return node.getRelations().stream()
            .map(relations -> new ConstraintToFields(
                new RootLevelConstraint(relations),
                SetUtils.setOf(relations.main(), relations.other())));
    }

    private Stream<ConstraintToFields> mapDecisionsToFields(ConstraintNode node) {
        return node.getDecisions()
            .stream()
            .map(decision -> new ConstraintToFields(
                new RootLevelConstraint(decision),
                FlatMappingSpliterator.flatMap(
                    FlatMappingSpliterator.flatMap(
                        decision.getOptions().stream(),
                        this::mapConstraintToFields),
                    objectField -> objectField.fields.stream()).collect(Collectors.toSet()))
            );
    }

    Map<RootLevelConstraint, Set<Field>> mapConstraintsToFields(DecisionTree decisionTree) {
        return mapConstraintToFields(decisionTree.getRootNode())
            .collect(
                Collectors.toMap(
                    map -> map.constraint,
                    map -> map.fields,
                    // in the case of the duplicate constraints(keys) use the existing one
                    (constraint1, constraint2) -> constraint1
                ));
    }
}
