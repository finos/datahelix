package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.databags.DataBag;

public interface IDataBagSource {
    Iterable<DataBag> generate(GenerationConfig generationConfig);
}
