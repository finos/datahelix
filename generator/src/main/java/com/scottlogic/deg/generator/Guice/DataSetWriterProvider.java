package com.scottlogic.deg.generator.Guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.outputs.dataset_writers.CsvDataSetWriter;
import com.scottlogic.deg.generator.outputs.dataset_writers.DataSetWriter;
import com.scottlogic.deg.generator.outputs.dataset_writers.MultiDataSetWriter;
import com.scottlogic.deg.generator.outputs.dataset_writers.SourceTracingDataSetWriter;

public class DataSetWriterProvider implements Provider<DataSetWriter> {
    private final GenerationConfigSource configSource;

    @Inject
    public DataSetWriterProvider(GenerationConfigSource configSource) {
        this.configSource = configSource;
    }

    @Override
    public DataSetWriter get() {
        DataSetWriter outputWriter = new CsvDataSetWriter();

        if (configSource.isEnableTracing()) {
            return new MultiDataSetWriter(outputWriter, new SourceTracingDataSetWriter());
        }
        return outputWriter;
    }
}
