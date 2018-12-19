package com.scottlogic.deg.generator.Guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.GenerationEngine;
import com.scottlogic.deg.generator.generation.DecisionTreeDataGenerator;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;

public class GenerationEngineProvider implements Provider<GenerationEngine> {

    private FileOutputTarget fileOutputTarget;
    private Provider<DecisionTreeDataGenerator> decisionTreeDataGenerator;

    @Inject
    public GenerationEngineProvider(FileOutputTarget fileOutputTarget,
                                    Provider<DecisionTreeDataGenerator> decisionTreeDataGenerator) {
        this.fileOutputTarget = fileOutputTarget;
        this.decisionTreeDataGenerator = decisionTreeDataGenerator;
    }

    @Override
    public GenerationEngine get() {
        return new GenerationEngine(
            fileOutputTarget,
            decisionTreeDataGenerator.get());
    }
}