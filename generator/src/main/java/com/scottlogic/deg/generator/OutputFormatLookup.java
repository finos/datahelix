package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.config.detail.OutputFormatOption;
import com.scottlogic.deg.generator.outputs.formats.OutputFormat;
import com.scottlogic.deg.generator.outputs.formats.csv.CsvOutputFormat;
import com.scottlogic.deg.generator.outputs.formats.json.JsonOutputFormat;
import com.scottlogic.deg.generator.validators.ValidationException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class OutputFormatLookup {
    public static final OutputFormatLookup standardLookup;

    static {
        Map<OutputFormatOption, OutputFormat> lookup = new HashMap<>();

        lookup.put(OutputFormatOption.CSV, new CsvOutputFormat());
        lookup.put(OutputFormatOption.JSON, new JsonOutputFormat());

        standardLookup = new OutputFormatLookup(lookup);
    }

    private final Map<OutputFormatOption, OutputFormat> codeToFormatObject;

    private OutputFormatLookup(Map<OutputFormatOption, OutputFormat> codeToFormatObject) {
        this.codeToFormatObject = Collections.unmodifiableMap(codeToFormatObject);
    }

    public OutputFormat get(OutputFormatOption formatCode) {
        OutputFormat correspondingFormat = codeToFormatObject.get(formatCode);

        if (correspondingFormat == null) {
            throw new ValidationException("Unrecognised output format");
        }

        return correspondingFormat;
    }
}
