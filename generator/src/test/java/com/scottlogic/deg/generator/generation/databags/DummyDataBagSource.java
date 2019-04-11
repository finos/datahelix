package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.generator.generation.GenerationConfig;

import java.util.stream.Stream;

class DummyDataBagSource implements DataBagSource
{
    private final Stream<Row> dataBags;

    public DummyDataBagSource(Row... rows) {
        this.dataBags = Stream.of(rows);
    }

    @Override
    public Stream<Row> generate(GenerationConfig generationConfig) {
        return this.dataBags;
    }
}
