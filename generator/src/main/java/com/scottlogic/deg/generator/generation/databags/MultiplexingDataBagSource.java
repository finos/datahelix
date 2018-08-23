package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.generator.generation.GenerationConfig;

import java.util.List;
import java.util.stream.Collectors;

public class MultiplexingDataBagSource implements IDataBagSource {
    private final List<IDataBagSource> subGenerators;

    public MultiplexingDataBagSource(List<IDataBagSource> subGenerators) {
        this.subGenerators = subGenerators;
    }

    @Override
    public Iterable<DataBag> generate(GenerationConfig generationConfig) {
        return generationConfig.getCombinationStrategy().permute(
            this.subGenerators.stream().map(sg -> sg.generate(generationConfig)).collect(Collectors.toList()));
    }
}
