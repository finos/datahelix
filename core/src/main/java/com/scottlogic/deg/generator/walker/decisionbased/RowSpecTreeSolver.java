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
package com.scottlogic.deg.generator.walker.decisionbased;

import com.google.inject.Inject;
import com.scottlogic.datahelix.generator.common.profile.Field;
import com.scottlogic.datahelix.generator.common.profile.Fields;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.walker.pruner.Merged;
import com.scottlogic.deg.generator.walker.pruner.TreePruner;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.scottlogic.datahelix.generator.common.util.FlatMappingSpliterator.flatMap;

public class RowSpecTreeSolver {

    private final ConstraintReducer constraintReducer;
    private final TreePruner treePruner;
    private final OptionPicker optionPicker;

    @Inject
    public RowSpecTreeSolver(ConstraintReducer constraintReducer,
                             TreePruner treePruner,
                             OptionPicker optionPicker) {
        this.constraintReducer = constraintReducer;
        this.treePruner = treePruner;
        this.optionPicker = optionPicker;
    }

    public Stream<RowSpec> createRowSpecs(DecisionTree tree) {
        return flatMap(reduceToRowNodes(tree.rootNode),
            rootNode -> toRowspec(tree.fields, rootNode));
    }

    private Stream<RowSpec> toRowspec(Fields fields, ConstraintNode rootNode) {
        Optional<RowSpec> result = constraintReducer.reduceConstraintsToRowSpec(fields, rootNode);
        return result.map(Stream::of).orElseGet(Stream::empty);
    }

    /**
     * a row node is a constraint node with no further decisions
     */
    private Stream<ConstraintNode> reduceToRowNodes(ConstraintNode rootNode) {
        if (rootNode.getDecisions().isEmpty()) {
            return Stream.of(rootNode);
        }

        DecisionNode decisionNode = optionPicker.pickDecision(rootNode);
        ConstraintNode rootWithoutDecision = rootNode.builder().removeDecision(decisionNode).build();

        Stream<ConstraintNode> rootOnlyConstraintNodes = optionPicker.streamOptions(decisionNode)
            .map(option -> combineWithRootNode(rootWithoutDecision, option))
            .filter(newNode -> !newNode.isContradictory())
            .map(Merged::get);

        return flatMap(
            rootOnlyConstraintNodes,
            this::reduceToRowNodes);
    }

    private Merged<ConstraintNode> combineWithRootNode(ConstraintNode rootNode, ConstraintNode option) {
        ConstraintNode constraintNode = rootNode.builder()
            .addDecisions(option.getDecisions())
            .addAtomicConstraints(option.getAtomicConstraints())
            .addRelations(option.getRelations())
            .build();

        return treePruner.pruneConstraintNode(constraintNode, getFields(option));
    }

    private Map<Field, FieldSpec> getFields(ConstraintNode option) {
        return option.getAtomicConstraints().stream()
            .map(AtomicConstraint::getField)
            .distinct()
            .collect(Collectors.toMap(
                Function.identity(),
                field -> FieldSpecFactory.fromType(field.getType())));
    }

}
