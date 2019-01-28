package com.scottlogic.deg.generator.generation;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeOptimiser;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.generation.object_generation.ObjectGenerator;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.walker.DecisionTreeWalker;

import java.util.stream.Stream;

public class DecisionTreeDataGenerator implements DataGenerator {
    private final DecisionTreeWalker treeWalker;
    private final DecisionTreeOptimiser treeOptimiser;
    private final ObjectGenerator objectGenerator;
    private final DataGeneratorMonitor monitor;

    @Inject
    public DecisionTreeDataGenerator(
            DecisionTreeWalker treeWalker,
            DecisionTreeOptimiser optimiser,
            ObjectGenerator objectGenerator,
            DataGeneratorMonitor monitor) {
        this.treeOptimiser = optimiser;
        this.treeWalker = treeWalker;
        this.objectGenerator = objectGenerator;
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
        Stream<GeneratedObject> dataRows = objectGenerator.generateObjectsFromRowSpecs(profile, rowSpecStream);

        return dataRows
            .limit(generationConfig.getMaxRows())
            .peek(this.monitor::rowEmitted);

    }
}
