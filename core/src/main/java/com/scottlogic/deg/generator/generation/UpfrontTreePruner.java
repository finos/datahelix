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

package com.scottlogic.deg.generator.generation;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.NodeMarking;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.validators.ContradictionDecisionTreeValidator;
import com.scottlogic.deg.generator.walker.pruner.Merged;
import com.scottlogic.deg.generator.walker.pruner.TreePruner;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UpfrontTreePruner {
    private TreePruner treePruner;
    private ContradictionDecisionTreeValidator validator;
    @Inject
    public UpfrontTreePruner(TreePruner treePruner, ContradictionDecisionTreeValidator validator) {
        this.treePruner = treePruner;
        this.validator = validator;
    }

    public DecisionTree runUpfrontPrune(DecisionTree tree, DataGeneratorMonitor monitor) {
        Map<Field, FieldSpec> fieldSpecs = tree.getFields().stream()
            .collect(
                Collectors.toMap(
                    Function.identity(),
                    f -> FieldSpecFactory.fromType(f.getType())));

        Merged<ConstraintNode> prunedNode = treePruner.pruneConstraintNode(tree.getRootNode(), fieldSpecs);
        DecisionTree markedTree = validator.markContradictions(tree);

        if (prunedNode.isContradictory()) {
            monitor.addLineToPrintAtEndOfGeneration("");
            monitor.addLineToPrintAtEndOfGeneration("The provided profile is wholly contradictory!");
            monitor.addLineToPrintAtEndOfGeneration("No data can be generated!");
            return new DecisionTree(null, tree.getFields());
        } else if (isPartiallyContradictory(markedTree.getRootNode())) {
            monitor.addLineToPrintAtEndOfGeneration("");
            monitor.addLineToPrintAtEndOfGeneration("The provided profile is partially contradictory!");
            monitor.addLineToPrintAtEndOfGeneration("Run the visualise command for more information.");
            return new DecisionTree(prunedNode.get(), tree.getFields());

        }
        return new DecisionTree(prunedNode.get(), tree.getFields());
    }

    private boolean isPartiallyContradictory(ConstraintNode root) {
        return
            root.hasMarking(NodeMarking.CONTRADICTORY) ||
            root.getDecisions().stream().anyMatch(this::isPartiallyContradictory);
    }

    private boolean isPartiallyContradictory(DecisionNode root) {
        return
            root.hasMarking(NodeMarking.CONTRADICTORY) ||
            root.getOptions().stream().anyMatch(this::isPartiallyContradictory);
    }
}
