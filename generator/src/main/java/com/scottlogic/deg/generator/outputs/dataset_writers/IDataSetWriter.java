package com.scottlogic.deg.generator.outputs.dataset_writers;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.outputs.GeneratedObject;

import java.io.IOException;
import java.nio.file.Path;

/** Something that can take a stream of data and persist it to a particular filepath; responsible for understanding specific file formats */
public interface IDataSetWriter {
    String makeFilename(
        String filenameWithoutExtension);

    void write(
        ProfileFields profileFields,
        Iterable<GeneratedObject> dataset,
        Path filePath) throws IOException;
}
