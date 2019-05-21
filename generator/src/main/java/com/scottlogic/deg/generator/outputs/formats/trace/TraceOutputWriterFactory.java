package com.scottlogic.deg.generator.outputs.formats.trace;

import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.outputs.formats.DataSetWriter;
import com.scottlogic.deg.generator.outputs.formats.OutputWriterFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

/** A format that writes metadata about the generated objects, rather than the objects themselves */
public class TraceOutputWriterFactory implements OutputWriterFactory {
    @Override
    public DataSetWriter createWriter(OutputStream stream, ProfileFields profileFields) throws IOException {
        return SourceTracingDataSetWriter.open(stream);
    }

    @Override
    public Optional<String> getFileExtensionWithoutDot() {
        return Optional.of("json");
    }
}
