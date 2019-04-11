package com.scottlogic.deg.generator;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeFactory;
import com.scottlogic.deg.generator.generation.*;
import com.scottlogic.deg.generator.generation.rows.Row;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;

import java.io.IOException;
import java.util.stream.Stream;

public class StandardGenerationEngine implements GenerationEngine {
    private final DecisionTreeFactory decisionTreeGenerator;
    private ReductiveDataGeneratorMonitor monitor;
    private final RowSolver rowSolver;

    @Inject
    public StandardGenerationEngine(
        RowSolver rowSolver,
        DecisionTreeFactory decisionTreeGenerator,
        ReductiveDataGeneratorMonitor monitor) {
        this.rowSolver = rowSolver;
        this.decisionTreeGenerator = decisionTreeGenerator;
        this.monitor = monitor;
    }

    public void generateDataSet(Profile profile, GenerationConfig config, OutputTarget outputTarget) throws IOException {
        final DecisionTree tree = decisionTreeGenerator.analyse(profile).getMergedTree();

        final Stream<Row> generatedDataItems = rowSolver.generateRows(profile, tree)
                .limit(config.getMaxRows().orElse(GenerationConfig.Constants.DEFAULT_MAX_ROWS))
                .peek(this.monitor::rowEmitted);

        monitor.generationStarting(config);
        outputTarget.outputDataset(generatedDataItems, profile.fields);
        monitor.endGeneration();
    }
}
