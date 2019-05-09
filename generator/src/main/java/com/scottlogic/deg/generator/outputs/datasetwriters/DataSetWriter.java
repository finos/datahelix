package com.scottlogic.deg.generator.outputs.datasetwriters;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.outputs.GeneratedObject;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;

public interface DataSetWriter<TWriter extends Closeable, T> {
    TWriter openWriter(
        Path directory,
        String fileName,
        ProfileFields profileFields) throws IOException;

    void writeRow(TWriter writer, GeneratedObject row, RowOutputFormatter<T> formatter) throws IOException;

    String getFileName(String fileNameWithoutExtension);
}
