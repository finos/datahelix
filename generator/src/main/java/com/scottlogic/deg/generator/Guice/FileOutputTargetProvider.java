package com.scottlogic.deg.generator.Guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.CommandLine.GenerateCommandLine;
import com.scottlogic.deg.generator.outputs.dataset_writers.CsvDataSetWriter;
import com.scottlogic.deg.generator.outputs.dataset_writers.DataSetWriter;
import com.scottlogic.deg.generator.outputs.dataset_writers.MultiDataSetWriter;
import com.scottlogic.deg.generator.outputs.dataset_writers.SourceTracingDataSetWriter;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;

public class FileOutputTargetProvider implements Provider<FileOutputTarget> {

    private GenerateCommandLine commandLine;

    @Inject
    FileOutputTargetProvider(GenerateCommandLine commandLine) {
        this.commandLine = commandLine;
    }

    @Override
    public FileOutputTarget get() {
        DataSetWriter outputWriter = new CsvDataSetWriter();

        if (this.commandLine.isEnableTracing()) {
            return new FileOutputTarget(commandLine.getOutputPath(), new MultiDataSetWriter(outputWriter, new SourceTracingDataSetWriter()));
        }
        return new FileOutputTarget(commandLine.getOutputPath(), outputWriter);
    }
}
