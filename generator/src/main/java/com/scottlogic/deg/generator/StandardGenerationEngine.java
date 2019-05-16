package com.scottlogic.deg.generator;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeFactory;
import com.scottlogic.deg.generator.generation.DataGenerator;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.generation.ReductiveDataGeneratorMonitor;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.outputs.formats.DataSetWriter;
import com.scottlogic.deg.generator.outputs.targets.SingleDatasetOutputTarget;

import java.io.IOException;
import java.util.stream.Stream;

public class StandardGenerationEngine {
    private final DecisionTreeFactory decisionTreeGenerator;
    private final ReductiveDataGeneratorMonitor monitor;
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

    public void generateDataSet(
        Profile profile,
        GenerationConfigSource config,
        SingleDatasetOutputTarget outputTarget)
        throws IOException {

        final DecisionTree decisionTree = this.decisionTreeGenerator.analyse(profile);

        final Stream<GeneratedObject> generatedDataItems =
            this.dataGenerator.generateData(profile, decisionTree, config);

        try (DataSetWriter writer = outputTarget.openWriter(profile.fields)) {
            generatedDataItems.forEach(row -> {
                try {
                    writer.writeRow(row);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        monitor.endGeneration();
    }
}
