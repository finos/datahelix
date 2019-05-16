package com.scottlogic.deg.generator.generation;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.FlatMappingSpliterator;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.treepartitioning.TreePartitioner;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.generation.combinationstrategies.CombinationStrategy;
import com.scottlogic.deg.generator.generation.databags.*;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.walker.DecisionTreeWalker;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DecisionTreeDataGenerator implements DataGenerator {
    private final DecisionTreeWalker treeWalker;
    private final DataGeneratorMonitor monitor;
    private final RowSpecDataBagGenerator dataBagSourceFactory;
    private final TreePartitioner treePartitioner;
    private final DecisionTreeOptimiser treeOptimiser;
    private final CombinationStrategy partitionCombiner;

    @Inject
    public DecisionTreeDataGenerator(
        DecisionTreeWalker treeWalker,
        TreePartitioner treePartitioner,
        DecisionTreeOptimiser optimiser,
        DataGeneratorMonitor monitor,
        RowSpecDataBagGenerator dataBagSourceFactory,
        CombinationStrategy combinationStrategy) {
        this.treePartitioner = treePartitioner;
        this.treeOptimiser = optimiser;
        this.treeWalker = treeWalker;
        this.monitor = monitor;
        this.dataBagSourceFactory = dataBagSourceFactory;
        this.partitionCombiner = combinationStrategy;
    }

    @Override
    public Stream<GeneratedObject> generateData(
        Profile profile,
        DecisionTree decisionTree,
        GenerationConfig generationConfig) {

        monitor.generationStarting();

        Stream<Stream<DataBag>> partitionedDataBags = treePartitioner
            .splitTreeIntoPartitions(decisionTree)
            .map(treeOptimiser::optimiseTree)
            .map(this::generateForPartition);

        return partitionCombiner.permute(partitionedDataBags)
            .map(dataBag -> convertToGeneratedObject(dataBag, profile.fields))
            .limit(generationConfig.getMaxRows())
            .peek(monitor::rowEmitted);
    }

    private Stream<DataBag> generateForPartition(DecisionTree tree) {
        Stream<RowSpec> rowSpecsForPartition = treeWalker.walk(tree);

        return FlatMappingSpliterator.flatMap(
            rowSpecsForPartition,
            dataBagSourceFactory::createDataBags);
    }

    private GeneratedObject convertToGeneratedObject(DataBag dataBag, ProfileFields fields) {
        return new GeneratedObject(
            fields.stream()
                .map(dataBag::getValueAndFormat)
                .collect(Collectors.toList()),
            dataBag.getRowSource(fields));
    }
}
