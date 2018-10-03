package com.scottlogic.deg.generator.outputs;

import com.scottlogic.deg.generator.ProfileFields;

import java.io.IOException;
import java.nio.file.Path;

public interface IDataSetWriter {
    String makeFilename(
        String filenameWithoutExtension);

    void write(
        ProfileFields profileFields,
        Iterable<GeneratedObject> dataset,
        Path filePath) throws IOException;
}
