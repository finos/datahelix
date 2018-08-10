package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.utils.ConcatenatingIterable;

import java.util.*;
import java.util.stream.Collectors;

/** Given a set of data bag sources, return a new one that concatenates the results of each one, in sequence */
public class ConcatenatingDataBagSource implements IDataBagSource {
    private final List<IDataBagSource> subSources;

    public ConcatenatingDataBagSource(List<IDataBagSource> subSources) {
        this.subSources = subSources;
    }

    @Override
    public Iterable<DataBag> generate(GenerationConfig generationConfig) {
        return new ConcatenatingIterable<>(
            this.subSources.stream()
                .map(sg -> sg.generate(generationConfig))
                .collect(Collectors.toList()));
    }
}
