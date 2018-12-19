package com.scottlogic.deg.generator.Guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.CommandLine.CanGenerate;
import com.scottlogic.deg.generator.generation.GenerationConfig;

public class GenerationConfigProvider implements Provider<GenerationConfig> {

    private CanGenerate commandLine;

    @Inject
    public GenerationConfigProvider(CanGenerate commandLine) {
        this.commandLine = commandLine;
    }

    @Override
    public GenerationConfig get() {
        return new GenerationConfig(
            commandLine.getGenerationType(),
            commandLine.getWalkerType(),
            commandLine.getCombinationType());
    }
}
