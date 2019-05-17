package com.scottlogic.deg.generator;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeFactory;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.generation.DataGenerator;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.ReductiveDataGeneratorMonitor;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.StringRestrictionsFactory;
import com.scottlogic.deg.generator.validators.ContradictionTreeValidator;
import com.scottlogic.deg.generator.validators.ContradictionValidationMonitor;

import java.io.IOException;
import java.util.stream.Stream;

public class StandardGenerationEngine implements GenerationEngine {
    private final DecisionTreeFactory decisionTreeGenerator;
    private ReductiveDataGeneratorMonitor monitor;
    private final DataGenerator dataGenerator;
    private final FieldSpecFactory fieldSpecFactory;
    private final FieldSpecMerger fieldSpecMerger;
    private final ContradictionTreeValidator treeValidator;

    @Inject
    public StandardGenerationEngine(
        DataGenerator dataGenerator,
        DecisionTreeFactory decisionTreeGenerator,
        ReductiveDataGeneratorMonitor monitor,
        FieldSpecFactory fieldSpecFactory,
        FieldSpecMerger fieldSpecMerger,
        ContradictionTreeValidator treeValidator) {
        this.dataGenerator = dataGenerator;
        this.decisionTreeGenerator = decisionTreeGenerator;
        this.monitor = monitor;
        this.fieldSpecFactory = fieldSpecFactory;
        this.fieldSpecMerger = fieldSpecMerger;
        this.treeValidator = treeValidator;
    }

    public void generateDataSet(Profile profile, GenerationConfig config, OutputTarget outputTarget) throws IOException {
        final DecisionTree decisionTree = this.decisionTreeGenerator.analyse(profile);

        treeValidator.reportContradictions(decisionTree);

        final Stream<GeneratedObject> generatedDataItems =
            this.dataGenerator.generateData(profile, decisionTree, config);

        outputTarget.outputDataset(generatedDataItems, profile.fields);

        monitor.endGeneration();
    }
}
