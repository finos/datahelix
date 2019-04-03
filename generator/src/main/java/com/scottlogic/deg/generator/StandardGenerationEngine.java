package com.scottlogic.deg.generator;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeCollection;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeFactory;
import com.scottlogic.deg.generator.generation.*;
import com.scottlogic.deg.generator.generation.databags.GeneratedObject;
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
        final DecisionTree analysedProfile = this.decisionTreeGenerator.analyse(profile).getMergedTree();

        final Stream<GeneratedObject> generatedDataItems =
            dataGenerator.generateData(profile, analysedProfile)

                .map(o->o.orderValues(profile.fields))
                .limit(config.getMaxRows().orElse(GenerationConfig.Constants.DEFAULT_MAX_ROWS))
                .peek(this.monitor::rowEmitted);

        monitor.generationStarting(config);
        outputTarget.outputDataset(generatedDataItems, profile.fields);
        monitor.endGeneration();
    }
}
