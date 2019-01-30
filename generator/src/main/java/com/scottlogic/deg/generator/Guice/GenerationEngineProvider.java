package com.scottlogic.deg.generator.Guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.GenerationEngine;
import com.scottlogic.deg.generator.StandardGenerationEngine;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.violations.ViolationGenerationEngine;

public class GenerationEngineProvider implements Provider<GenerationEngine> {

    private GenerationConfigSource configSource;
    private GenerationEngine generationEngine;
    private GenerationEngine violationEngine;

    @Inject
    public GenerationEngineProvider(GenerationConfigSource configSource,
                                    StandardGenerationEngine generationEngine,
                                    ViolationGenerationEngine violationEngine) {
        this.configSource = configSource;
        this.generationEngine = generationEngine;
        this.violationEngine = violationEngine;
    }

    @Override
    public GenerationEngine get() {
        if (configSource.shouldViolate()) {
            return this.violationEngine;
        }
        return this.generationEngine;
    }
}
