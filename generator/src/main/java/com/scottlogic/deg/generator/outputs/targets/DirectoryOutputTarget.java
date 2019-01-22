package com.scottlogic.deg.generator.outputs.targets;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.outputs.TestCaseGenerationResult;
import com.scottlogic.deg.generator.outputs.TestCaseGenerationResultWriter;
import com.scottlogic.deg.generator.outputs.dataset_writers.DataSetWriter;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

/** Output into a specific directory */
public class DirectoryOutputTarget implements OutputTarget {
    private final Path directoryPath;
    private final DataSetWriter dataSetWriter;
    private final TestCaseGenerationResultWriter testCaseWriter;

    @Inject
    public DirectoryOutputTarget(
        @Named("outputPath") Path directoryPath,
        DataSetWriter dataSetWriter) {

        this.directoryPath = directoryPath;
        this.dataSetWriter = dataSetWriter;
        this.testCaseWriter = new TestCaseGenerationResultWriter(this.dataSetWriter);
    }

    @Override
    public void outputDataset(Stream<GeneratedObject> generatedObjects, ProfileFields profileFields) throws IOException {
        try (Closeable writer = this.dataSetWriter.openWriter(this.directoryPath, this.dataSetWriter.getFileName("output"), profileFields)) {
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
    public void outputTestCases(TestCaseGenerationResult dataSets) throws IOException {
        this.testCaseWriter
            .writeToDirectory(
                dataSets,
                this.directoryPath.toAbsolutePath().normalize());
    }
}
