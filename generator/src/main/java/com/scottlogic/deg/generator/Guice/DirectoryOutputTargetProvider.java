package com.scottlogic.deg.generator.Guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.outputs.dataset_writers.DataSetWriter;
import com.scottlogic.deg.generator.outputs.targets.DirectoryOutputTarget;

public class DirectoryOutputTargetProvider implements Provider<DirectoryOutputTarget> {
    private final GenerationConfigSource commandLine;
    private final DataSetWriter dataSetWriter;

    @Inject
    public DirectoryOutputTargetProvider(GenerationConfigSource commandLine, DataSetWriter dataSetWriter) {
        this.commandLine = commandLine;
        this.dataSetWriter = dataSetWriter;
    }

    @Override
    public DirectoryOutputTarget get() {
        return new DirectoryOutputTarget(
            this.commandLine.getOutputPath(),
            dataSetWriter
        );
    }
}
