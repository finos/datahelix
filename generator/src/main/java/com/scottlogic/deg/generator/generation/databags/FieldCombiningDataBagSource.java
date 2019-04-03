package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.generator.generation.GenerationConfig;

import java.util.List;
import java.util.stream.Stream;

/** Given a list of streams of GeneratedObjects, each list referring to a single field.
 *  return a single stream of GeneratedObjects with all the different fields added */
public class FieldCombiningDataBagSource implements DataBagSource {
    private final List<Stream<GeneratedObject>> subGenerators;

    FieldCombiningDataBagSource(List<Stream<GeneratedObject>> subGenerators) {
        this.subGenerators = subGenerators;
    }

    @Override
    public Stream<GeneratedObject> generate(GenerationConfig generationConfig) {
        return generationConfig.getCombinationStrategy()
            .permute(subGenerators.stream());
    }
}
