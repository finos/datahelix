package com.scottlogic.deg.generator.outputs;

import com.scottlogic.deg.generator.Profile;

import java.io.IOException;
import java.nio.file.Path;

public interface IDataSetWriter {
    String write(
            Profile profile,
            TestCaseDataSet dataset,
            Path directoryPath,
            String filenameWithoutExtension) throws IOException;
}
