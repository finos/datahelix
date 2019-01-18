package com.scottlogic.deg.generator.generation;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeOptimiser;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.generation.databags.ConcatenatingDataBagSource;
import com.scottlogic.deg.generator.generation.databags.DataBagSource;
import com.scottlogic.deg.generator.generation.databags.MultiplexingDataBagSource;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.walker.DecisionTreeWalker;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DecisionTreeDataGenerator implements DataGenerator {
    private final DecisionTreeWalker treeWalker;
    private final DataGeneratorMonitor monitor;
    private final DecisionTreeOptimiser treeOptimiser;

    @Inject
    public DecisionTreeDataGenerator(
            DecisionTreeWalker treeWalker,
            DecisionTreeOptimiser optimiser,
            DataGeneratorMonitor monitor) {
        this.treeOptimiser = optimiser;
        this.treeWalker = treeWalker;
        this.monitor = monitor;
    }

    @Override
    public Stream<GeneratedObject> generateData(
        Profile profile,
        DecisionTree decisionTree,
        GenerationConfig generationConfig) {

        final List<DecisionTree> partitionedTrees = Stream.of(decisionTree)
                    .map(this.treeOptimiser::optimiseTree)
                .collect(Collectors.toList());

        final Stream<Stream<RowSpec>> rowSpecsByPartition = partitionedTrees
            .stream()
            .map(treeWalker::walk);

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
