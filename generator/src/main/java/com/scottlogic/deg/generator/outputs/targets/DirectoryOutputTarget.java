package com.scottlogic.deg.generator.outputs.targets;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.outputs.TestCaseGenerationResult;
import com.scottlogic.deg.generator.outputs.TestCaseGenerationResultWriter;
import com.scottlogic.deg.generator.outputs.dataset_writers.IDataSetWriter;

import java.io.IOException;
import java.nio.file.Path;

/** Output into a specific directory */
public class DirectoryOutputTarget implements IOutputTarget {
    private final Path directoryPath;
    private final IDataSetWriter dataSetWriter;

    public DirectoryOutputTarget(Path directoryPath, IDataSetWriter dataSetWriter) {
        this.directoryPath = directoryPath;
        this.dataSetWriter = dataSetWriter;
    }

    @Override
    public void outputDataset(Iterable<GeneratedObject> generatedObjects, ProfileFields profileFields) throws IOException {
        Path outputPath = this.directoryPath
            .resolve(this.dataSetWriter.makeFilename("output"));

        this.dataSetWriter.write(profileFields, generatedObjects, outputPath);
    }

    @Override
    public void outputTestCases(TestCaseGenerationResult dataSets) throws IOException {
        new TestCaseGenerationResultWriter(this.dataSetWriter)
            .writeToDirectory(
                dataSets,
                this.directoryPath.toAbsolutePath().normalize());
    }
}
