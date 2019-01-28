package com.scottlogic.deg.generator.Guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.scottlogic.deg.generator.CommandLine.GenerateCommandLine;
import com.scottlogic.deg.generator.GenerationEngine;

public class GenerationEngineProvider implements Provider<GenerationEngine> {

    private GenerateCommandLine commandLine;
    private GenerationEngine generationEngine;
    private GenerationEngine violationEngine;

    @Inject
    public GenerationEngineProvider(GenerateCommandLine commandLine,
                                    @Named("valid") GenerationEngine generationEngine,
                                    @Named("invalid") GenerationEngine violationEngine) {
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
