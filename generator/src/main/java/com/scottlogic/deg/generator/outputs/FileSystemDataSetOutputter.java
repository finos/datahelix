package com.scottlogic.deg.generator.outputs;

import com.scottlogic.deg.generator.ProfileFields;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSystemDataSetOutputter implements IDataSetOutputter {
    private final String directoryPath;

    public FileSystemDataSetOutputter(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    @Override
    public void outputDataset(Iterable<GeneratedObject> generatedObjects, ProfileFields profileFields) throws IOException {
        IDataSetWriter writer = new CsvDataSetWriter();

        Path outputPath = Paths.get(this.directoryPath)
            .resolve(writer.makeFilename("output"));

        new CsvDataSetWriter().write(profileFields, generatedObjects, outputPath);
    }

    public void outputTestCases(TestCaseGenerationResult dataSets) throws IOException {
        new TestCaseGenerationResultWriter(new CsvDataSetWriter())
            .writeToDirectory(
                dataSets,
                Paths.get(this.directoryPath).toAbsolutePath().normalize());
    }
}
