package com.scottlogic.deg.generator.Guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.CommandLine.GenerateCommandLine;
import com.scottlogic.deg.generator.outputs.dataset_writers.CsvDataSetWriter;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;

public class FileOutputTargetProvider implements Provider<FileOutputTarget> {

    private GenerateCommandLine commandLine;

    @Inject
    FileOutputTargetProvider(GenerateCommandLine commandLine) {
        this.commandLine = commandLine;
    }

    @Override
    public FileOutputTarget get() {
        return new FileOutputTarget(commandLine.getOutputPath(), new CsvDataSetWriter());
    }
}
