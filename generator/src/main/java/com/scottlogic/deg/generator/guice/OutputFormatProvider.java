package com.scottlogic.deg.generator.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.outputs.formats.OutputFormat;
import com.scottlogic.deg.generator.outputs.formats.csv.CsvOutputFormat;
import com.scottlogic.deg.generator.outputs.formats.json.JsonOutputFormat;

public class OutputFormatProvider implements Provider<OutputFormat> {
    private final GenerationConfigSource configSource;
    private final CsvOutputFormat csvOutputFormat;
    private final JsonOutputFormat jsonOutputFormat;

    @Inject
    public OutputFormatProvider(
        GenerationConfigSource configSource,
        CsvOutputFormat csvOutputFormat,
        JsonOutputFormat jsonOutputFormat) {

        this.configSource = configSource;
        this.csvOutputFormat = csvOutputFormat;
        this.jsonOutputFormat = jsonOutputFormat;
    }

    @Override
    public OutputFormat get() {
        switch (configSource.getOutputFormat()){
            case CSV:
                return csvOutputFormat;
            case JSON:
                return jsonOutputFormat;
        }

        throw new RuntimeException(String.format("Unknown output format %s, options are CSV or JSON", configSource.getOutputFormat()));
    }
}
