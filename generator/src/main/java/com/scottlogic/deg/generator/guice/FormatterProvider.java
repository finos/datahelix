package com.scottlogic.deg.generator.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.outputs.datasetwriters.CsvFormatter;
import com.scottlogic.deg.generator.outputs.datasetwriters.JsonFormatter;
import com.scottlogic.deg.generator.outputs.datasetwriters.RowOutputFormatter;

public class FormatterProvider implements Provider<RowOutputFormatter> {

    private final GenerationConfigSource configSource;
    private final CsvFormatter csvFormatter;
    private final JsonFormatter jsonFormatter;

    @Inject
    public FormatterProvider(GenerationConfigSource configSource, CsvFormatter csvFormatter, JsonFormatter jsonFormatter) {
        this.configSource = configSource;
        this.csvFormatter = csvFormatter;
        this.jsonFormatter = jsonFormatter;
    }

    @Override
    public RowOutputFormatter get() {
        switch (configSource.getOutputFormat()) {
            case CSV:
                return csvFormatter;
            case JSON:
                return jsonFormatter;
        }

        throw new RuntimeException(String.format("Unknown output format %s, options are CSV or JSON", configSource.getOutputFormat()));
    }
}
