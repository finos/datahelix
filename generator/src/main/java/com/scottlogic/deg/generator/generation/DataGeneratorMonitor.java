package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.common.output.GeneratedObject;

public interface DataGeneratorMonitor {
    default void generationStarting() {}
    default void rowEmitted(GeneratedObject row) {}
    default void endGeneration() {}
}
