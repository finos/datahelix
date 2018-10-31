package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.generator.generation.GenerationConfig;

import java.util.stream.Stream;

public interface IDataBagSource {
    Stream<DataBag> generate(GenerationConfig generationConfig);
}
