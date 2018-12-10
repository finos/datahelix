package com.scottlogic.deg.generator.outputs.targets;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.outputs.TestCaseGenerationResult;
import com.scottlogic.deg.generator.outputs.TestCaseGenerationResultWriter;
import com.scottlogic.deg.generator.outputs.dataset_writers.IDataSetWriter;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

/** Output into a specific directory */
public class DirectoryOutputTarget implements IOutputTarget {
    private final Path directoryPath;
    private final IDataSetWriter dataSetWriter;
    private final TestCaseGenerationResultWriter testCaseWriter;

    public DirectoryOutputTarget(
        Path directoryPath,
        String profileFileNameWithoutExtension,
        IDataSetWriter dataSetWriter) {

        this.directoryPath = directoryPath;
        this.dataSetWriter = dataSetWriter;
        this.testCaseWriter = new TestCaseGenerationResultWriter(
            this.dataSetWriter,
            profileFileNameWithoutExtension);
    }

    @Override
    public void outputDataset(Stream<GeneratedObject> generatedObjects, ProfileFields profileFields) throws IOException {
        try (Closeable writer = this.dataSetWriter.openWriter(this.directoryPath, "output", profileFields)) {
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
