package com.scottlogic.deg.generator.generation;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.FlatMappingSpliterator;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.treepartitioning.TreePartitioner;
import com.scottlogic.deg.generator.generation.combinationstrategies.CombinationStrategy;
import com.scottlogic.deg.generator.generation.databags.*;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.walker.DecisionTreeWalker;
import com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy.FixFieldStrategy;
import com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy.FixFieldStrategyFactory;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DecisionTreeDataGenerator implements DataGenerator {
    private final DecisionTreeWalker treeWalker;
    private final DataGeneratorMonitor monitor;
    private final RowSpecDataBagGenerator dataBagSourceFactory;
    private final TreePartitioner treePartitioner;
    private final DecisionTreeOptimiser treeOptimiser;
    private final FixFieldStrategyFactory walkerStrategyFactory;

    @Inject
    public DecisionTreeDataGenerator(
        DecisionTreeWalker treeWalker,
        TreePartitioner treePartitioner,
        DecisionTreeOptimiser optimiser,
        DataGeneratorMonitor monitor,
        RowSpecDataBagGenerator dataBagSourceFactory,
        FixFieldStrategyFactory walkerStrategyFactory) {
        this.treePartitioner = treePartitioner;
        this.treeOptimiser = optimiser;
        this.treeWalker = treeWalker;
        this.monitor = monitor;
        this.dataBagSourceFactory = dataBagSourceFactory;
        this.walkerStrategyFactory = walkerStrategyFactory;
    }

    @Override
    public Stream<GeneratedObject> generateData(
        Profile profile,
        DecisionTree decisionTree,
        GenerationConfig generationConfig) {

        monitor.generationStarting(generationConfig);
        CombinationStrategy partitionCombiner = generationConfig.getCombinationStrategy();

        Stream<Stream<DataBag>> partitionedDataBags = treePartitioner
            .splitTreeIntoPartitions(decisionTree)
            .map(treeOptimiser::optimiseTree)
            .map(tree -> generateForPartition(profile, tree, generationConfig));

        return partitionCombiner.permute(partitionedDataBags)
            .map(dataBag -> convertToGeneratedObject(profile, dataBag))
            .limit(generationConfig.getMaxRows().orElse(GenerationConfig.Constants.DEFAULT_MAX_ROWS))
            .peek(monitor::rowEmitted);
    }

    private Stream<DataBag> generateForPartition(Profile profile, DecisionTree tree, GenerationConfig config) {
        FixFieldStrategy fixFieldStrategy = walkerStrategyFactory.getWalkerStrategy(profile, tree, config);

        Stream<Stream<DataBag>> dataBagSources = treeWalker.walk(tree, fixFieldStrategy)
            .map(dataBagSourceFactory::createDataBags);

        return FlatMappingSpliterator.flatMap(
            dataBagSources,
            Function.identity());
    }

    private GeneratedObject convertToGeneratedObject(Profile profile, DataBag dataBag) {
        return new GeneratedObject(
            profile.fields.stream()
                .map(dataBag::getValueAndFormat)
                .collect(Collectors.toList()),
            dataBag.getRowSource(profile.fields));
    }
}
