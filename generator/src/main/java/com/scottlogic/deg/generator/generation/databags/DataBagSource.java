package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.generator.generation.GenerationConfig;

import java.util.stream.Stream;

public interface DataBagSource {
    Stream<DataBag> generate(GenerationConfig generationConfig);
}
