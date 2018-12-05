package com.scottlogic.deg.generator.outputs.targets;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.outputs.TestCaseGenerationResult;
import com.scottlogic.deg.generator.outputs.dataset_writers.IDataSetWriter;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public class FileOutputTarget implements IOutputTarget {
    private final Path filePath;
    private final IDataSetWriter dataSetWriter;

    public FileOutputTarget(Path filePath, IDataSetWriter dataSetWriter) {
        this.filePath = filePath;
        this.dataSetWriter = dataSetWriter;
    }

    @Override
    public void outputDataset(
        Stream<GeneratedObject> generatedObjects,
        ProfileFields profileFields)
        throws IOException {

        try (Closeable writer = this.dataSetWriter.openWriter(filePath, profileFields)) {
            generatedObjects.forEach(row -> {
                try {
                    this.dataSetWriter.writeRow(writer, row);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @Override
    public void outputTestCases(
        TestCaseGenerationResult dataSets) {
        throw new UnsupportedOperationException();
    }
}
