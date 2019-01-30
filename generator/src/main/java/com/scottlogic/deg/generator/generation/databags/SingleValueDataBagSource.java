package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.generator.generation.GenerationConfig;

import java.util.stream.Stream;

class SingleValueDataBagSource implements DataBagSource {
    private final DataBagSource source;

    SingleValueDataBagSource(DataBagSource source) {
        this.source = source;
    }

    @Override
    public Stream<DataBag> generate(GenerationConfig generationConfig) {
        return source.generate(generationConfig)
            .limit(1);
    }
}
