package com.scottlogic.deg.output.writer;

import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.output.writer.DataSetWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

/** Represents a file format in which data could be output - e.g. CSV, JSON. */
public interface OutputWriterFactory {
    DataSetWriter createWriter(
        OutputStream stream,
        ProfileFields profileFields) throws IOException;

    Optional<String> getFileExtensionWithoutDot();
}
