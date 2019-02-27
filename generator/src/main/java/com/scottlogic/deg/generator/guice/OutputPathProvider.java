package com.scottlogic.deg.generator.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;

import java.nio.file.Path;

public class OutputPathProvider implements Provider<Path> {
    final
    GenerationConfigSource configSource;

    @Inject
    public OutputPathProvider(GenerationConfigSource configSource){
        this.configSource = configSource;
    }

    @Override
    public Path get() {
        return configSource.getOutputPath();
    }
}
