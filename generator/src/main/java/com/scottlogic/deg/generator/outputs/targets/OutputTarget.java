package com.scottlogic.deg.generator.outputs.targets;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.generator.outputs.GeneratedObject;

import java.io.IOException;
import java.util.stream.Stream;

public interface OutputTarget {
    void outputDataset(Stream<GeneratedObject> generatedObjects, ProfileFields profileFields) throws IOException;
}
