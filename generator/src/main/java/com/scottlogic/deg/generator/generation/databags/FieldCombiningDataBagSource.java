package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.generator.generation.GenerationConfig;

import java.util.List;
import java.util.stream.Stream;

public class FieldCombiningDataBagSource implements DataBagSource {
    private final List<Stream<GeneratedObject>> subGenerators;

    public FieldCombiningDataBagSource(List<Stream<GeneratedObject>> subGenerators) {
        this.subGenerators = subGenerators;
    }

    @Override
    public Stream<GeneratedObject> generate(GenerationConfig generationConfig) {
        return generationConfig.getCombinationStrategy()
            .permute(subGenerators.stream());
    }
}
