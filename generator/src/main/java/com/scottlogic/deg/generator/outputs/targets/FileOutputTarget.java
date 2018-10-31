package com.scottlogic.deg.generator.outputs.targets;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.outputs.TestCaseGenerationResult;
import com.scottlogic.deg.generator.outputs.dataset_writers.IDataSetWriter;

import java.io.IOException;
import java.nio.file.Path;

/** Output to a specific file */
public class FileOutputTarget implements IOutputTarget {
    private final Path filePath;
    private final IDataSetWriter dataSetWriter;

    public FileOutputTarget(Path filePath, IDataSetWriter dataSetWriter) {
        this.filePath = filePath;
        this.dataSetWriter = dataSetWriter;
    }

    @Override
    public void outputDataset(
        Iterable<GeneratedObject> generatedObjects,
        ProfileFields profileFields)
        throws IOException {

        this.dataSetWriter.write(profileFields, generatedObjects, filePath);
    }

    @Override
    public void outputTestCases(
        TestCaseGenerationResult dataSets)
        throws IOException {

        throw new UnsupportedOperationException();
    }
}
