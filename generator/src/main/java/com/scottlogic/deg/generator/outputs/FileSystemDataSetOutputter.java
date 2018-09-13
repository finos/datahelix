package com.scottlogic.deg.generator.outputs;

import java.io.IOException;
import java.nio.file.Paths;

public class FileSystemDataSetOutputter implements IDataSetOutputter {
    private final String directoryPath;

    public FileSystemDataSetOutputter(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public void output(TestCaseGenerationResult dataSets) throws IOException {
        new TestCaseGenerationResultWriter().writeToDirectory(
            dataSets,
            Paths.get(this.directoryPath).toAbsolutePath().normalize());
    }
}
