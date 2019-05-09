package com.scottlogic.deg.generator.outputs.datasetwriters;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.outputs.GeneratedObject;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;

public interface DataSetWriter<TWriter extends Closeable, C> {
    TWriter openWriter(
        Path directory,
        String fileName,
        ProfileFields profileFields) throws IOException;

    void writeRow(TWriter writer, GeneratedObject row, C formatter) throws IOException;

    String getFileName(String fileNameWithoutExtension);
}
