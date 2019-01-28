package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;

import java.io.IOException;

public interface GenerationEngine {
    void generateDataSet(Profile profile, GenerationConfig config, FileOutputTarget fileOutputTarget) throws IOException;
}
