package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.generator.generation.GenerationConfig;

import java.util.Arrays;
import java.util.List;

class DummyDataBagSource implements IDataBagSource
{
    private final List<DataBag> dataBags;

    public DummyDataBagSource(List<DataBag> dataBags) {
        this.dataBags = dataBags;
    }

    public DummyDataBagSource(DataBag... dataBags) {
        this.dataBags = Arrays.asList(dataBags);
    }

    @Override
    public Iterable<DataBag> generate(GenerationConfig generationConfig) {
        return this.dataBags;
    }
}
