package com.scottlogic.deg.generator;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeFactory;
import com.scottlogic.deg.generator.generation.DataGenerator;
import com.scottlogic.deg.generator.outputs.targets.DirectoryOutputTarget;

public class DirectoryOutputtingGenerationEngine extends GenerationEngine {
    @Inject
    public DirectoryOutputtingGenerationEngine(
        DirectoryOutputTarget outputter,
        DataGenerator dataGenerator,
        DecisionTreeFactory decisionTreeGenerator) {
        super(outputter, dataGenerator, decisionTreeGenerator);
    }
}
