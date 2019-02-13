package com.scottlogic.deg.generator.generation;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeOptimiser;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.generation.row_generation.RowGenerator;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.walker.DecisionTreeWalker;

import java.util.stream.Stream;

public class DecisionTreeDataGenerator implements DataGenerator {
    private final DecisionTreeWalker treeWalker;
    private final DataGeneratorMonitor monitor;
    private final RowGenerator rowGenerator;
    private final DecisionTreeOptimiser treeOptimiser;

    @Inject
    public DecisionTreeDataGenerator(
        DecisionTreeWalker treeWalker,
        DecisionTreeOptimiser optimiser,
        RowGenerator rowGenerator,
        DataGeneratorMonitor monitor) {
        this.treeOptimiser = optimiser;
        this.treeWalker = treeWalker;
        this.rowGenerator = rowGenerator;
        this.monitor = monitor;
    }

    @Override
    public Stream<GeneratedObject> generateData(
        Profile profile,
        DecisionTree decisionTree,
        GenerationConfig generationConfig) {

        monitor.generationStarting(generationConfig);

        DecisionTree optimisedTree = treeOptimiser.optimiseTree(decisionTree);
        Stream<RowSpec> rowSpecStream = treeWalker.walk(optimisedTree);
        Stream<GeneratedObject> dataRows = rowGenerator.generateObjectsFromRowSpecs(profile, rowSpecStream);

        return dataRows
            .limit(generationConfig.getMaxRows().orElse(GenerationConfig.Constants.DEFAULT_MAX_ROWS))
            .peek(this.monitor::rowEmitted);

    }
}
