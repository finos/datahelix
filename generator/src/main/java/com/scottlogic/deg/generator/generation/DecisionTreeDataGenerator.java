package com.scottlogic.deg.generator.generation;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.generator.decisiontree.*;
import com.scottlogic.deg.generator.decisiontree.treepartitioning.TreePartitioner;
import com.scottlogic.deg.generator.generation.combinationstrategies.CombinationStrategy;
import com.scottlogic.deg.generator.generation.databags.*;
import com.scottlogic.deg.common.output.GeneratedObject;
import com.scottlogic.deg.generator.validators.ContradictionTreeValidator;
import com.scottlogic.deg.generator.walker.DecisionTreeWalker;

import java.util.stream.Stream;

public class DecisionTreeDataGenerator implements DataGenerator {
    private final DecisionTreeWalker treeWalker;
    private final DataGeneratorMonitor monitor;
    private final ContradictionTreeValidator treeValidator;
    private final DecisionTreeFactory decisionTreeGenerator;
    private final TreePartitioner treePartitioner;
    private final DecisionTreeOptimiser treeOptimiser;
    private final CombinationStrategy partitionCombiner;
    private final long maxRows;

    @Inject
    public DecisionTreeDataGenerator(
        DecisionTreeFactory decisionTreeGenerator,
        DecisionTreeWalker treeWalker,
        TreePartitioner treePartitioner,
        DecisionTreeOptimiser optimiser,
        DataGeneratorMonitor monitor,
        ContradictionTreeValidator treeValidator,
        CombinationStrategy combinationStrategy,
        @Named("config:maxRows") long maxRows) {
        this.decisionTreeGenerator = decisionTreeGenerator;
        this.treePartitioner = treePartitioner;
        this.treeOptimiser = optimiser;
        this.treeWalker = treeWalker;
        this.monitor = monitor;
        this.treeValidator = treeValidator;
        this.partitionCombiner = combinationStrategy;
        this.maxRows = maxRows;
    }

    @Override
    public Stream<GeneratedObject> generateData(Profile profile) {
        monitor.generationStarting();
        DecisionTree decisionTree = decisionTreeGenerator.analyse(profile);

        Node contradictingNode = treeValidator.reportContradictions(decisionTree);
        if (decisionTree.getRootNode().equals(contradictingNode)) {
            // Entire profile is contradictory.
            monitor.addLineToPrintAtEndOfGeneration("The provided profile is wholly contradictory. No fields can successfully be fixed.");
            return Stream.empty();
        } else if (contradictingNode != null) {
            // Part of the profile is contradictory, and therefore could be simplified.
            monitor.addLineToPrintAtEndOfGeneration("Warning: There is a partial contradiction in the profile. The contradicting section is:");
            monitor.addLineToPrintAtEndOfGeneration(contradictingNode.toString());
        }

        Stream<Stream<DataBag>> partitionedDataBags = treePartitioner
            .splitTreeIntoPartitions(decisionTree)
            .map(treeOptimiser::optimiseTree)
            .map(treeWalker::walk);

        return partitionCombiner.permute(partitionedDataBags)
            .map(d->(GeneratedObject)d)
            .limit(maxRows)
            .peek(monitor::rowEmitted);
    }
}
