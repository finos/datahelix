package com.scottlogic.deg.generator;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeFactory;
import com.scottlogic.deg.generator.generation.DataGenerator;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.ReductiveDataGeneratorMonitor;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;

import java.io.IOException;
import java.util.stream.Stream;

public class StandardGenerationEngine implements GenerationEngine {
    private final DecisionTreeFactory decisionTreeGenerator;
    private ReductiveDataGeneratorMonitor monitor;
    private final DataGenerator dataGenerator;

    @Inject
    public StandardGenerationEngine(
        DataGenerator dataGenerator,
        DecisionTreeFactory decisionTreeGenerator,
        ReductiveDataGeneratorMonitor monitor) {
        this.dataGenerator = dataGenerator;
        this.decisionTreeGenerator = decisionTreeGenerator;
        this.monitor = monitor;
    }

    public void generateDataSet(Profile profile, GenerationConfig config, OutputTarget outputTarget) throws IOException {
        final DecisionTree decisionTree = this.decisionTreeGenerator.analyse(profile);

        final Stream<GeneratedObject> generatedDataItems =
            this.dataGenerator.generateData(profile, decisionTree, config);

        outputTarget.outputDataset(generatedDataItems, profile.fields);

        monitor.endGeneration();
    }
}
