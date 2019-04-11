package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.generator.generation.GenerationConfig;

import java.util.stream.Stream;

class DummyRowSource implements RowSource
{
    private final Stream<Row> dataBags;

    public DummyRowSource(Row... rows) {
        this.dataBags = Stream.of(rows);
    }

    @Override
    public Stream<Row> generate(GenerationConfig generationConfig) {
        return this.dataBags;
    }
}
