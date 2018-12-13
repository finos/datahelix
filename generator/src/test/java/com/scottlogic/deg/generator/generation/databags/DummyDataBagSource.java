package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.generator.generation.GenerationConfig;

import java.util.stream.Stream;

class DummyDataBagSource implements DataBagSource
{
    private final Stream<DataBag> dataBags;

    public DummyDataBagSource(Stream<DataBag> dataBags) {
        this.dataBags = dataBags;
    }

    public DummyDataBagSource(DataBag... dataBags) {
        this.dataBags = Stream.of(dataBags);
    }

    @Override
    public Stream<DataBag> generate(GenerationConfig generationConfig) {
        return this.dataBags;
    }
}
