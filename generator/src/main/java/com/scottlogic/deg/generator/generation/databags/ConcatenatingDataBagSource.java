package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.utils.ConcatenatingIterable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** Given a set of data bag sources, return a new one that concatenates the results of each one, in sequence */
public class ConcatenatingDataBagSource implements IDataBagSource {
    private final Stream<IDataBagSource> subSources;

    public ConcatenatingDataBagSource(Stream<IDataBagSource> subSources) {
        this.subSources = subSources;
    }

    @Override
    public Stream<DataBag> generate(GenerationConfig generationConfig) {
        return subSources
            .map(source -> source.generate(generationConfig))
            .reduce(Stream::concat)
            .orElse(Stream.empty());
    }
}
