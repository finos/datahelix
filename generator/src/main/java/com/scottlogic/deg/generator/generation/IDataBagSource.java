package com.scottlogic.deg.generator.generation;

public interface IDataBagSource {
    Iterable<DataBag> generate(GenerationConfig generationConfig);
}
