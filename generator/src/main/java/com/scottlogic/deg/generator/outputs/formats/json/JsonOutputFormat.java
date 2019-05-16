package com.scottlogic.deg.generator.outputs.formats.json;

import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.outputs.formats.DataSetWriter;
import com.scottlogic.deg.generator.outputs.formats.OutputFormat;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

public class JsonOutputFormat implements OutputFormat {
    @Override
    public DataSetWriter createWriter(OutputStream stream, ProfileFields profileFields) throws IOException {
        return JsonDataSetWriter.open(stream, profileFields);
    }

    @Override
    public Optional<String> getFileExtensionWithoutDot() {
        return Optional.of("json");
    }
}
