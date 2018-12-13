package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.generator.generation.GenerationConfig;

import java.util.stream.Stream;

public class MultiplexingDataBagSource implements DataBagSource {
    private final Stream<DataBagSource> subGenerators;

    public MultiplexingDataBagSource(Stream<DataBagSource> subGenerators) {
        this.subGenerators = subGenerators;
    }

    @Override
    public Stream<DataBag> generate(GenerationConfig generationConfig) {

        return generationConfig.getCombinationStrategy().permute(
            this.subGenerators
                .map(sg -> sg.generate(generationConfig)));
    }
}
