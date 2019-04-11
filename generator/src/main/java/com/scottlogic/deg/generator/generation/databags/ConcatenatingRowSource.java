package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.generator.FlatMappingSpliterator;
import com.scottlogic.deg.generator.generation.GenerationConfig;

import java.util.stream.Stream;

/** Given a set of row sources, return a new one that concatenates the results of each one, in sequence */
public class ConcatenatingRowSource implements RowSource {
    private final Stream<RowSource> subSources;

    public ConcatenatingRowSource(Stream<RowSource> subSources) {
        this.subSources = subSources;
    }

    @Override
    public Stream<Row> generate(GenerationConfig generationConfig) {
        return FlatMappingSpliterator.flatMap(
            this.subSources
                .map(source -> source.generate(generationConfig)),
            streamOfStreams -> streamOfStreams);
    }
}
