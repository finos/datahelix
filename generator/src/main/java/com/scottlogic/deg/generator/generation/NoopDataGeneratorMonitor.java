package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.outputs.GeneratedObject;

public class NoopDataGeneratorMonitor implements DataGeneratorMonitor {

    @Override
    public void generationStarting(GenerationConfig generationConfig) { }

    @Override
    public void rowEmitted(GeneratedObject row) { }
}
