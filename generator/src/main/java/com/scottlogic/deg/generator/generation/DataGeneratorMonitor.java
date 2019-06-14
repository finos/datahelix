package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.common.output.GeneratedObject;

import java.io.PrintStream;

public interface DataGeneratorMonitor {
    default void generationStarting() {}
    default void rowEmitted(GeneratedObject row) {}
    default void endGeneration() {}
    void addLineToPrintAtEndOfGeneration(String line, PrintStream writer);
}
