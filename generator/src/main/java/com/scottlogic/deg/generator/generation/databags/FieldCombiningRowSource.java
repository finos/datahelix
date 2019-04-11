package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.generator.generation.GenerationConfig;

import java.util.List;
import java.util.stream.Stream;

/**
 * Given a list of streams of rows, each list referring to a single field.
 *  return a single stream of rows with all the different fields added
 */
public class FieldCombiningRowSource implements RowSource {
    private final List<Stream<Row>> subGenerators;

    FieldCombiningRowSource(List<Stream<Row>> subGenerators) {
        this.subGenerators = subGenerators;
    }

    @Override
    public Stream<Row> generate(GenerationConfig generationConfig) {
        return generationConfig.getCombinationStrategy()
            .permute(subGenerators.stream());
    }
}
