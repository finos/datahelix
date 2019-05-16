package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.outputs.GeneratedObject;

public interface DataGeneratorMonitor {
    void generationStarting(GenerationConfigSource generationConfig);
    void rowEmitted(GeneratedObject row);
    void endGeneration();
}

