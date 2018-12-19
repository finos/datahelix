package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeFactory;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.TreePartitioner;
import com.scottlogic.deg.generator.generation.databags.ConcatenatingDataBagSource;
import com.scottlogic.deg.generator.generation.databags.DataBagSource;
import com.scottlogic.deg.generator.generation.databags.MultiplexingDataBagSource;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.restrictions.RowSpec;
import com.scottlogic.deg.generator.walker.DecisionTreeWalker;
import com.scottlogic.deg.generator.walker.DecisionTreeWalkerFactory;
import com.scottlogic.deg.generator.walker.RuntimeDecisionTreeWalkerFactory;
import com.scottlogic.deg.generator.walker.reductive.field_selection_strategy.FixFieldStrategy;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DecisionTreeDataGenerator implements DataGenerator {
    private final GenerationConfig config;
    private final DataGeneratorMonitor monitor;
    private final TreePartitioner treePartitioner;
    private final DecisionTreeOptimiser treeOptimiser;
    private final DecisionTreeFactory profileAnalyser;
    private final FixFieldStrategy fixFieldStrategy;

    public DecisionTreeDataGenerator(
        GenerationConfig config,
        TreePartitioner treePartitioner,
        DecisionTreeOptimiser optimiser,
        DataGeneratorMonitor monitor,
        DecisionTreeFactory profileAnalyser,
        FixFieldStrategy fixFieldStrategy) {
        this.config = config;
        this.treePartitioner = treePartitioner;
        this.treeOptimiser = optimiser;
        this.monitor = monitor;
        this.profileAnalyser  = profileAnalyser;
        this.fixFieldStrategy  = fixFieldStrategy;
    }

    @Override
    public Stream<GeneratedObject> generateData(
        Profile profile,
        GenerationConfig generationConfig) {
        DecisionTree decisionTree = profileAnalyser.analyse(profile).getMergedTree();

        // Factory should be IOC
        DecisionTreeWalkerFactory treeWalkerFactory = new RuntimeDecisionTreeWalkerFactory(config, monitor, fixFieldStrategy);

        final List<DecisionTree> partitionedTrees =
            treePartitioner
                .splitTreeIntoPartitions(decisionTree)
                    .map(this.treeOptimiser::optimiseTree)
                .collect(Collectors.toList());

        final Stream<Stream<RowSpec>> rowSpecsByPartition = partitionedTrees
            .stream()
            .map(treeWalkerFactory.getDecisionTreeWalker(decisionTree)::walk);

        final Stream<DataBagSource> allDataBagSources =
            rowSpecsByPartition
                .map(rowSpecs ->
                    new ConcatenatingDataBagSource(
                        rowSpecs
                            .map(RowSpec::createDataBagSource)));

        Stream<GeneratedObject> dataRows = new MultiplexingDataBagSource(allDataBagSources)
            .generate(generationConfig)
            .map(dataBag -> new GeneratedObject(
                profile.fields.stream()
                    .map(dataBag::getValueAndFormat)
                    .collect(Collectors.toList()),
                dataBag.getRowSource(profile.fields)));

        monitor.generationStarting(generationConfig);

        return dataRows
            .limit(generationConfig.getMaxRows())
            .peek(this.monitor::rowEmitted);

    }
}
