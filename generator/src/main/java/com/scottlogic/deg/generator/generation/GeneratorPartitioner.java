package com.scottlogic.deg.generator.generation;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.treepartitioning.TreePartitioner;
import com.scottlogic.deg.generator.generation.combinationstrategies.CombinationStrategy;
import com.scottlogic.deg.generator.generation.databags.*;

import java.util.function.BiFunction;
import java.util.stream.Stream;

/**
 * Splits a Tree into subtrees, Generates data for each tree
 * Combines the data for each tree returns a stream of fully generated data
 */
public class GeneratorPartitioner {
    private final TreePartitioner treePartitioner;
    private final DecisionTreeOptimiser treeOptimiser;
    private final GenerationConfig generationConfig;

    @Inject
    public GeneratorPartitioner(
        TreePartitioner treePartitioner,
        DecisionTreeOptimiser optimiser,
        GenerationConfig generationConfig) {
        this.treePartitioner = treePartitioner;
        this.treeOptimiser = optimiser;
        this.generationConfig = generationConfig;
    }

    public Stream<GeneratedObject> partitionThenGenerate(Profile profile, DecisionTree decisionTree, BiFunction<Profile, DecisionTree, Stream<GeneratedObject>> generator) {
        CombinationStrategy partitionCombiner = generationConfig.getCombinationStrategy();

        final Stream<Stream<GeneratedObject>> partitionedGeneratedObjects =
            treePartitioner
                .splitTreeIntoPartitions(decisionTree)
                .map(this.treeOptimiser::optimiseTree)
                .map(tree -> generator.apply(profile, tree));

        return partitionCombiner
            .permute(partitionedGeneratedObjects);
    }
}
