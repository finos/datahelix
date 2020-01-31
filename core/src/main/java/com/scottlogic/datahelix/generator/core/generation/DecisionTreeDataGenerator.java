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

package com.scottlogic.datahelix.generator.core.generation;

import com.google.inject.Inject;
import com.scottlogic.datahelix.generator.common.output.GeneratedObject;
import com.scottlogic.datahelix.generator.core.config.detail.VisualiserLevel;
import com.scottlogic.datahelix.generator.core.decisiontree.DecisionTree;
import com.scottlogic.datahelix.generator.core.decisiontree.DecisionTreeFactory;
import com.scottlogic.datahelix.generator.core.decisiontree.DecisionTreeOptimiser;
import com.scottlogic.datahelix.generator.core.decisiontree.treepartitioning.TreePartitioner;
import com.scottlogic.datahelix.generator.core.generation.combinationstrategies.CombinationStrategy;
import com.scottlogic.datahelix.generator.core.generation.databags.DataBag;
import com.scottlogic.datahelix.generator.core.generation.visualiser.Visualiser;
import com.scottlogic.datahelix.generator.core.generation.visualiser.VisualiserFactory;
import com.scottlogic.datahelix.generator.core.profile.Profile;
import com.scottlogic.datahelix.generator.core.walker.DecisionTreeWalker;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class DecisionTreeDataGenerator implements DataGenerator {
    private static final String INITIAL_TREE_VISUALISER_TITLE = "01_Initial_Tree";
    private static final String PRUNED_TREE_VISUALISER_TITLE = "02_Pruned_Tree";
    private final DecisionTreeWalker treeWalker;
    private final DataGeneratorMonitor monitor;
    private final DecisionTreeFactory decisionTreeGenerator;
    private final TreePartitioner treePartitioner;
    private final DecisionTreeOptimiser treeOptimiser;
    private final CombinationStrategy partitionCombiner;
    private final UpfrontTreePruner upfrontTreePruner;
    private final VisualiserFactory visualiserFactory;

    @Inject
    public DecisionTreeDataGenerator(
        DecisionTreeFactory decisionTreeGenerator,
        DecisionTreeWalker treeWalker,
        TreePartitioner treePartitioner,
        DecisionTreeOptimiser optimiser,
        DataGeneratorMonitor monitor,
        CombinationStrategy combinationStrategy,
        UpfrontTreePruner upfrontTreePruner,
        VisualiserFactory visualiserFactory) {
        this.decisionTreeGenerator = decisionTreeGenerator;
        this.treePartitioner = treePartitioner;
        this.treeOptimiser = optimiser;
        this.treeWalker = treeWalker;
        this.monitor = monitor;
        this.partitionCombiner = combinationStrategy;
        this.upfrontTreePruner = upfrontTreePruner;
        this.visualiserFactory = visualiserFactory;
    }

    @Override
    public Stream<GeneratedObject> generateData(Profile profile) {
        DecisionTree decisionTree = decisionTreeGenerator.analyse(profile);
        visualiseTree(decisionTree,  INITIAL_TREE_VISUALISER_TITLE);

        decisionTree = upfrontTreePruner.runUpfrontPrune(decisionTree, monitor);
        visualiseTree(decisionTree, PRUNED_TREE_VISUALISER_TITLE);
        if (decisionTree.getRootNode() == null) {
            return Stream.empty();
        }

        Stream<Supplier<Stream<DataBag>>> partitionedDataBags = treePartitioner
            .splitTreeIntoPartitions(decisionTree)
            .map(treeOptimiser::optimiseTree)
            .map(tree -> () -> treeWalker.walk(tree));

        //noinspection RedundantCast
        return partitionCombiner.permute(partitionedDataBags)
            .map(d-> (GeneratedObject)d);
    }

    private void visualiseTree(DecisionTree decisionTree, String title) {
        try (Visualiser visualiser = visualiserFactory.create(VisualiserLevel.STANDARD, title)) {
            visualiser.printTree(title, decisionTree);
        }
    }
}
