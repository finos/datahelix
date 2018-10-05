package com.scottlogic.deg.generator.outputs.targets;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.outputs.TestCaseGenerationResult;

import java.io.IOException;

/** Where the generated data should be persisted. Possible implementations: A filepath, a directory path, a DB connection string */
public interface IOutputTarget {
    void outputDataset(
        Iterable<GeneratedObject> generatedObjects,
        ProfileFields profileFields)
        throws IOException;

    void outputTestCases(
        TestCaseGenerationResult dataSets)
        throws IOException;
}
