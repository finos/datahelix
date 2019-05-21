package com.scottlogic.deg.generator.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.output.writer.OutputWriterFactory;
import com.scottlogic.deg.output.writer.csv.CsvOutputWriterFactory;
import com.scottlogic.deg.output.writer.json.JsonOutputWriterFactory;

public class OutputWriterFactoryProvider implements Provider<OutputWriterFactory> {
    private final GenerationConfigSource configSource;
    private final CsvOutputWriterFactory csvOutputWriterFactory;
    private final JsonOutputWriterFactory jsonOutputWriterFactory;

    @Inject
    public OutputWriterFactoryProvider(
        GenerationConfigSource configSource,
        CsvOutputWriterFactory csvOutputWriterFactory,
        JsonOutputWriterFactory jsonOutputWriterFactory) {

        this.configSource = configSource;
        this.csvOutputWriterFactory = csvOutputWriterFactory;
        this.jsonOutputWriterFactory = jsonOutputWriterFactory;
    }

    @Override
    public OutputWriterFactory get() {
        switch (configSource.getOutputFormat()){
            case CSV:
                return csvOutputWriterFactory;
            case JSON:
                return jsonOutputWriterFactory;
        }

        throw new RuntimeException(String.format("Unknown output format %s, options are CSV or JSON", configSource.getOutputFormat()));
    }
}
