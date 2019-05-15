package com.scottlogic.deg.generator.outputs.formats;

import com.scottlogic.deg.generator.ProfileFields;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

/** Represents a file format in which data could be output - e.g. CSV, JSON. */
public interface OutputFormat {
    DataSetWriter createWriter(
        OutputStream stream,
        ProfileFields profileFields) throws IOException;

    Optional<String> getFileExtensionWithoutDot();
}
