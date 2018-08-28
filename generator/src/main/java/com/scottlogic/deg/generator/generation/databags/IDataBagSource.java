package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.generator.generation.GenerationConfig;

public interface IDataBagSource {
    Iterable<DataBag> generate(GenerationConfig generationConfig);
}
