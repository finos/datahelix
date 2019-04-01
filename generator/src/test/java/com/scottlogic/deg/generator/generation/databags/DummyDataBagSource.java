package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.generator.generation.GenerationConfig;

import java.util.stream.Stream;

class DummyDataBagSource implements DataBagSource
{
    private final Stream<GeneratedObject> dataBags;

    public DummyDataBagSource(GeneratedObject... generatedObjects) {
        this.dataBags = Stream.of(generatedObjects);
    }

    @Override
    public Stream<GeneratedObject> generate(GenerationConfig generationConfig) {
        return this.dataBags;
    }
}
