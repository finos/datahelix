package com.scottlogic.deg.generator.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.outputs.datasetwriters.*;

public class DataSetWriterProvider implements Provider<DataSetWriter> {
    private final GenerationConfigSource configSource;
    private final CsvDataSetWriter csvWriter;
    private final JsonDataSetWriter jsonWriter;

    @Inject
    public DataSetWriterProvider(GenerationConfigSource configSource, CsvDataSetWriter csvWriter, JsonDataSetWriter jsonWriter) {
        this.configSource = configSource;
        this.csvWriter = csvWriter;
        this.jsonWriter = jsonWriter;
    }

    @Override
    public DataSetWriter get() {
        DataSetWriter outputWriter = getBasicDataSetWriter();

        if (configSource.isEnableTracing()) {
            return new MultiDataSetWriter(outputWriter, new SourceTracingDataSetWriter());
        }
        return outputWriter;
    }

    private DataSetWriter getBasicDataSetWriter() {
        switch (configSource.getOutputFormat()){
            case CSV:
                return csvWriter;
            case JSON:
                return jsonWriter;
        }

        throw new RuntimeException(String.format("Unknown output format %s, options are CSV or JSON", configSource.getOutputFormat()));
    }
}
