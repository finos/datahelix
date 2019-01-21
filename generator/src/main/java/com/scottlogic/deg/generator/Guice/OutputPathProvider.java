package com.scottlogic.deg.generator.Guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;

import java.nio.file.Path;

public class OutputPathProvider implements Provider<Path> {
    final
    GenerationConfigSource commandLine;

    @Inject
    public OutputPathProvider(GenerationConfigSource commandLine){
        this.commandLine = commandLine;
    }

    @Override
    public Path get() {
        return commandLine.getOutputPath();
    }
}
