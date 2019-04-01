package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.generation.databags.GeneratedObject;

public interface DataGeneratorMonitor {
    void generationStarting(GenerationConfig generationConfig);
    void rowEmitted(GeneratedObject row);
    void endGeneration();
}

