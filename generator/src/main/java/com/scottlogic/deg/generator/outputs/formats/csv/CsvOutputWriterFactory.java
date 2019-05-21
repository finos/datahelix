package com.scottlogic.deg.generator.outputs.formats.csv;

import com.scottlogic.deg.common.profile.ProfileFields;
    import com.scottlogic.deg.generator.outputs.formats.DataSetWriter;
    import com.scottlogic.deg.generator.outputs.formats.OutputWriterFactory;

    import java.io.IOException;
    import java.io.OutputStream;
    import java.util.Optional;

public class CsvOutputWriterFactory implements OutputWriterFactory {
    @Override
    public DataSetWriter createWriter(OutputStream stream, ProfileFields profileFields) throws IOException {
        return CsvDataSetWriter.open(stream, profileFields);
    }

    @Override
    public Optional<String> getFileExtensionWithoutDot() {
        return Optional.of("csv");
    }
}
