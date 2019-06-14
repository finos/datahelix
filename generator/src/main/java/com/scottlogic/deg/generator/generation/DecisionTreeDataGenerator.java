package com.scottlogic.deg.generator.generation;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.scottlogic.deg.common.output.GeneratedObject;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeFactory;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.Node;
import com.scottlogic.deg.generator.decisiontree.treepartitioning.TreePartitioner;
import com.scottlogic.deg.generator.generation.combinationstrategies.CombinationStrategy;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.validators.ContradictionTreeValidator;
import com.scottlogic.deg.generator.walker.DecisionTreeWalker;

import java.util.Collection;
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

        decisionTree = reportThenCullContradictions(decisionTree);
        if (decisionTree.getRootNode() == null) {
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

    private DecisionTree reportThenCullContradictions(DecisionTree decisionTree) {
        Collection<Node> contradictingNodes = treeValidator.reportContradictions(decisionTree);
        if (contradictingNodes.contains(decisionTree.getRootNode())) {
            // Entire profile is contradictory.
            monitor.addLineToPrintAtEndOfGeneration("The provided profile is wholly contradictory. No fields can successfully be fixed.");
            return new DecisionTree(null, decisionTree.getFields(), decisionTree.getDescription());
        } else if (contradictingNodes.size() > 0) {
            // Part of the profile is contradictory, and therefore could be simplified.
            monitor.addLineToPrintAtEndOfGeneration(
                "Warning: There are " +
                    contradictingNodes.size() +
                    " partial contradiction(s) in the profile. The contradicting section(s) are:"
            );
            for (Node node : contradictingNodes) {
                monitor.addLineToPrintAtEndOfGeneration(node.toString());
            }
            // TODO: Remove all contradicting subtrees in the decision tree and return the new tree.
            // Currently the tree is being returned without removing any parts.
            return decisionTree;
        }
        // No contradictions.
        return decisionTree;
    }
}
