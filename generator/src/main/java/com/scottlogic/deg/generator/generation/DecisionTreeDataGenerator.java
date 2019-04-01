package com.scottlogic.deg.generator.generation;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.treepartitioning.TreePartitioner;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.generation.databags.*;
import com.scottlogic.deg.generator.walker.DecisionTreeWalker;
import com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy.FixFieldStrategyFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DecisionTreeDataGenerator implements DataGenerator {
    private final DecisionTreeWalker treeWalker;
    private final DataGeneratorMonitor monitor;
    private final RowSpecDataBagSourceFactory dataBagSourceFactory;
    private final TreePartitioner treePartitioner;
    private final DecisionTreeOptimiser treeOptimiser;
    private final FixFieldStrategyFactory walkerStrategyFactory;

    @Inject
    public DecisionTreeDataGenerator(
        DecisionTreeWalker treeWalker,
        TreePartitioner treePartitioner,
        DecisionTreeOptimiser optimiser,
        DataGeneratorMonitor monitor,
        RowSpecDataBagSourceFactory dataBagSourceFactory,
        FixFieldStrategyFactory walkerStrategyFactory) {
        this.treePartitioner = treePartitioner;
        this.treeOptimiser = optimiser;
        this.treeWalker = treeWalker;
        this.monitor = monitor;
        this.dataBagSourceFactory = dataBagSourceFactory;
        this.walkerStrategyFactory = walkerStrategyFactory;
    }

    private Stream<GeneratedObject> getDataBagSourceForTree(Profile profile, DecisionTree tree, GenerationConfig generationConfig){
        Stream<RowSpec> walked = treeWalker.walk(tree,
            walkerStrategyFactory.getWalkerStrategy(profile, tree, generationConfig));

        return new ConcatenatingDataBagSource(
            walked.map(dataBagSourceFactory::createDataBagSource))
            .generate(generationConfig);
    }

    @Override
    public Stream<GeneratedObject> generateData(
        Profile profile,
        DecisionTree decisionTree,
        GenerationConfig generationConfig) {

        final List<DecisionTree> partitionedTrees =
            treePartitioner
                .splitTreeIntoPartitions(decisionTree)
                .map(this.treeOptimiser::optimiseTree)
                .collect(Collectors.toList());

        final Stream<Stream<GeneratedObject>> allDataBagSources = partitionedTrees
            .stream()
            .map(tree -> getDataBagSourceForTree(profile, tree, generationConfig));

        Stream<GeneratedObject> dataRows = generationConfig.getCombinationStrategy()
            .permute(allDataBagSources)
            .map(o->o.orderValues(profile.fields));

        monitor.generationStarting(generationConfig);

        return dataRows
            .limit(generationConfig.getMaxRows().orElse(GenerationConfig.Constants.DEFAULT_MAX_ROWS))
            .peek(this.monitor::rowEmitted);

    }
}
