package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.treepartitioning.TreePartitioner;
import com.scottlogic.deg.generator.generation.combinationstrategies.CombinationStrategy;
import com.scottlogic.deg.generator.generation.databags.*;

import java.util.stream.Stream;

public class PartitioningDataGeneratorDecorator implements DataGenerator {
    private final TreePartitioner treePartitioner;
    private final DecisionTreeOptimiser treeOptimiser;
    private final DataGenerator underlying;
    private final GenerationConfig generationConfig;

    public PartitioningDataGeneratorDecorator(
        DataGenerator underlying,
        TreePartitioner treePartitioner,
        DecisionTreeOptimiser optimiser,
        GenerationConfig generationConfig) {
        this.treePartitioner = treePartitioner;
        this.treeOptimiser = optimiser;
        this.underlying = underlying;
        this.generationConfig = generationConfig;
    }

    @Override
    public Stream<GeneratedObject> generateData(
        Profile profile,
        DecisionTree decisionTree) {
        CombinationStrategy partitionCombiner = generationConfig.getCombinationStrategy();

        final Stream<Stream<GeneratedObject>> partitionedGeneratedObjects =
            treePartitioner
                .splitTreeIntoPartitions(decisionTree)
                .map(this.treeOptimiser::optimiseTree)
                .map(tree -> underlying.generateData(profile, tree));

        return partitionCombiner
            .permute(partitionedGeneratedObjects);
    }
}
