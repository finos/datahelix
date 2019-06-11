package com.scottlogic.deg.generator.generation;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.generator.decisiontree.*;
import com.scottlogic.deg.generator.decisiontree.treepartitioning.TreePartitioner;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.generation.combinationstrategies.CombinationStrategy;
import com.scottlogic.deg.generator.generation.databags.*;
import com.scottlogic.deg.common.output.GeneratedObject;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.validators.ContradictionTreeValidator;
import com.scottlogic.deg.generator.validators.ContradictionValidationMonitor;
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
        FieldSpecFactory fieldSpecFactory,
        FieldSpecMerger fieldSpecMerger,
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
            // Contradiction
            System.out.println("There is a hard contradiction in the profile. No data can be generated at all.");
            return Stream.empty();
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
