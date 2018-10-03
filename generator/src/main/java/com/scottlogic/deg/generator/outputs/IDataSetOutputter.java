package com.scottlogic.deg.generator.outputs;

import com.scottlogic.deg.generator.ProfileFields;

import java.io.IOException;

public interface IDataSetOutputter {
    void outputDataset(
        Iterable<GeneratedObject> generatedObjects,
        ProfileFields profileFields)
        throws IOException;

    void outputTestCases(
        TestCaseGenerationResult dataSets)
        throws IOException;
}
