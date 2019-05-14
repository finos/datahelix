package com.scottlogic.deg.generator;

import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;

import java.io.IOException;

public interface GenerationEngine {
    void generateDataSet(Profile profile, GenerationConfig config, OutputTarget OutputTarget) throws IOException;
}
