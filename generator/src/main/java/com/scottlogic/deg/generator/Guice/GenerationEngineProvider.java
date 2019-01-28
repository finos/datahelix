package com.scottlogic.deg.generator.Guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.GenerationEngine;
import com.scottlogic.deg.generator.StandardGenerationEngine;
import com.scottlogic.deg.generator.ViolationGenerationEngine;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;

public class GenerationEngineProvider implements Provider<GenerationEngine> {

    private GenerationConfigSource commandLine;
    private GenerationEngine generationEngine;
    private GenerationEngine violationEngine;

    @Inject
    public GenerationEngineProvider(GenerationConfigSource commandLine,
                                    StandardGenerationEngine generationEngine,
                                    ViolationGenerationEngine violationEngine) {
        this.commandLine = commandLine;
        this.generationEngine = generationEngine;
        this.violationEngine = violationEngine;
    }

    @Override
    public GenerationEngine get() {
        if (commandLine.shouldViolate()) {
            return this.violationEngine;
        }
        return this.generationEngine;
    }
}
