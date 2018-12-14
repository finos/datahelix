package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.generator.generation.GenerationConfig;

import java.util.stream.Stream;

/** Given a set of data bag sources, return a new one that concatenates the results of each one, in sequence */
public class ConcatenatingDataBagSource implements DataBagSource {
    private final Stream<DataBagSource> subSources;

    public ConcatenatingDataBagSource(Stream<DataBagSource> subSources) {
        this.subSources = subSources;
    }

    @Override
    public Stream<DataBag> generate(GenerationConfig generationConfig) {
        return this.subSources
            .map(source -> source.generate(generationConfig))
            .flatMap(streamOfStreams -> streamOfStreams);
    }
}
