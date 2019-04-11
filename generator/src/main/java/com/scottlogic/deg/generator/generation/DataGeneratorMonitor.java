package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.generation.rows.Row;

public interface DataGeneratorMonitor {
    void generationStarting(GenerationConfig generationConfig);
    void rowEmitted(Row row);
    void endGeneration();
}

