package com.scottlogic.deg.generator.cucumber.utils;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.outputs.TestCaseGenerationResult;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;

import java.io.IOException;
import java.util.stream.Stream;

public class InMemoryOutputTarget implements OutputTarget {

    private Stream<GeneratedObject> generatedObjects;

    @Override
    public void outputDataset(Stream<GeneratedObject> generatedObjects, ProfileFields profileFields) throws IOException {
        this.generatedObjects = generatedObjects;
    }

    @Override
    public void outputTestCases(TestCaseGenerationResult dataSets) throws IOException {
    }

    public Object getRows() {
        return this.generatedObjects;
    }
}
