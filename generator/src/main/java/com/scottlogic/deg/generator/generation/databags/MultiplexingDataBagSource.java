package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.generator.generation.GenerationConfig;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MultiplexingDataBagSource implements IDataBagSource {
    private final Stream<IDataBagSource> subGenerators;

    public MultiplexingDataBagSource(Stream<IDataBagSource> subGenerators) {
        this.subGenerators = subGenerators;
    }

    @Override
    public Iterable<DataBag> generate(GenerationConfig generationConfig) {

        return generationConfig.getCombinationStrategy().permute(
            this.subGenerators
                .map(sg -> sg.generate(generationConfig)));
    }
}
