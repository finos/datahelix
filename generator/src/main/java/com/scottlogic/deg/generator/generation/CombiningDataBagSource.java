package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.generation.combination_strategies.ICombinationStrategy;

import java.util.List;
import java.util.stream.Collectors;

class CombiningDataBagSource implements IDataBagSource {
    private final List<IDataBagSource> subGenerators;

    CombiningDataBagSource(List<IDataBagSource> subGenerators) {
        this.subGenerators = subGenerators;
    }

    @Override
    public Iterable<DataBag> generate(GenerationConfig generationConfig) {
        return generationConfig.getCombinationStrategy().permute(
            this.subGenerators.stream().map(sg -> sg.generate(generationConfig)).collect(Collectors.toList()));
    }
}
