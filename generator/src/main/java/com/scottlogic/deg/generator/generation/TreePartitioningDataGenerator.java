package com.scottlogic.deg.generator.generation;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.treepartitioning.TreePartitioner;
import com.scottlogic.deg.generator.generation.combinationstrategies.CombinationStrategy;
import com.scottlogic.deg.generator.generation.databags.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TreePartitioningDataGenerator implements DataGenerator {
    private final DataGeneratorMonitor monitor;
    private final TreePartitioner treePartitioner;
    private final DecisionTreeOptimiser treeOptimiser;
    private final TreeWalkingDataGenerator underlying;

    @Inject
    public TreePartitioningDataGenerator(
        TreeWalkingDataGenerator underlying,
        TreePartitioner treePartitioner,
        DecisionTreeOptimiser optimiser,
        DataGeneratorMonitor monitor) {
        this.treePartitioner = treePartitioner;
        this.treeOptimiser = optimiser;
        this.underlying = underlying;
        this.monitor = monitor;
    }

    @Override
    public Stream<GeneratedObject> generateData(
        Profile profile,
        DecisionTree decisionTree,
        GenerationConfig generationConfig) {
        CombinationStrategy partitionCombiner = generationConfig.getCombinationStrategy();

        final List<DecisionTree> partitionedTrees =
            treePartitioner
                .splitTreeIntoPartitions(decisionTree)
                .map(this.treeOptimiser::optimiseTree)
                .collect(Collectors.toList());

        final Stream<Stream<GeneratedObject>> allDataBagSources =
            partitionedTrees.stream()
            .map(tree -> underlying.generateData(profile, tree, generationConfig));

        return partitionCombiner
            .permute(allDataBagSources)
            .map(o->o.orderValues(profile.fields))
            .limit(generationConfig.getMaxRows().orElse(GenerationConfig.Constants.DEFAULT_MAX_ROWS))
            .peek(this.monitor::rowEmitted);
    }
}
