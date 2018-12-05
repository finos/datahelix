package com.scottlogic.deg.generator.outputs.dataset_writers;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.outputs.GeneratedObject;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;

public interface IDataSetWriter<TWriter extends Closeable> {
    String makeFilename(
        String filenameWithoutExtension);

    TWriter openWriter(
        Path filePath, ProfileFields profileFields) throws IOException;

    void writeRow(TWriter writer, GeneratedObject row) throws IOException;
}
