package com.scottlogic.deg.generator.Guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;

public class GenerationConfigProvider implements Provider<GenerationConfig> {

    private GenerationConfigSource commandLine;

    @Inject
    public GenerationConfigProvider(GenerationConfigSource commandLine) {
        this.commandLine = commandLine;
    }

    @Override
    public GenerationConfig get() {

        return new GenerationConfig(commandLine);
    }
}
