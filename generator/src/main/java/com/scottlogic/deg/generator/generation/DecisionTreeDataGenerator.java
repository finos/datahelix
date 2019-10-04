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
import com.google.inject.name.Named;
import com.scottlogic.deg.common.output.GeneratedObject;
import com.scottlogic.deg.generator.profile.Profile;
import com.scottlogic.deg.generator.config.detail.VisualiserLevel;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeFactory;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.treepartitioning.TreePartitioner;
import com.scottlogic.deg.generator.generation.combinationstrategies.CombinationStrategy;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.generation.visualiser.Visualiser;
import com.scottlogic.deg.generator.generation.visualiser.VisualiserFactory;
import com.scottlogic.deg.generator.walker.DecisionTreeWalker;

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
    private final long maxRows;

    @Inject
    public DecisionTreeDataGenerator(
        DecisionTreeFactory decisionTreeGenerator,
        DecisionTreeWalker treeWalker,
        TreePartitioner treePartitioner,
        DecisionTreeOptimiser optimiser,
        DataGeneratorMonitor monitor,
        CombinationStrategy combinationStrategy,
        UpfrontTreePruner upfrontTreePruner,
        VisualiserFactory visualiserFactory,
        @Named("config:maxRows") long maxRows) {
        this.decisionTreeGenerator = decisionTreeGenerator;
        this.treePartitioner = treePartitioner;
        this.treeOptimiser = optimiser;
        this.treeWalker = treeWalker;
        this.monitor = monitor;
        this.partitionCombiner = combinationStrategy;
        this.upfrontTreePruner = upfrontTreePruner;
        this.visualiserFactory = visualiserFactory;
        this.maxRows = maxRows;
    }

    @Override
    public Stream<GeneratedObject> generateData(Profile profile) {
        monitor.generationStarting();

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

        return partitionCombiner.permute(partitionedDataBags)
            .map(d->(GeneratedObject)d)
            .limit(maxRows)
            .peek(monitor::rowEmitted);
    }

    private void visualiseTree(DecisionTree decisionTree, String title) {
        try (Visualiser visualiser = visualiserFactory.create(VisualiserLevel.STANDARD, title)) {
            visualiser.printTree(title, decisionTree);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
