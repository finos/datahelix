package com.scottlogic.deg.generator.outputs.targets;

import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.outputs.GeneratedObject;

import java.io.IOException;
import java.util.stream.Stream;

public interface OutputTarget {
    void outputDataset(Stream<GeneratedObject> generatedObjects, ProfileFields profileFields) throws IOException;
}
