package com.scottlogic.deg.generator.Guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.outputs.dataset_writers.DataSetWriter;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;

public class FileOutputTargetProvider implements Provider<FileOutputTarget> {
    private final GenerationConfigSource commandLine;
    private final DataSetWriter dataSetWriter;

    @Inject
    public FileOutputTargetProvider(
        GenerationConfigSource commandLine,
        DataSetWriter dataSetWriter) {
        this.commandLine = commandLine;
        this.dataSetWriter = dataSetWriter;
    }

    @Override
    public FileOutputTarget get() {
        return new FileOutputTarget(commandLine.getOutputPath(), dataSetWriter);
    }
}

