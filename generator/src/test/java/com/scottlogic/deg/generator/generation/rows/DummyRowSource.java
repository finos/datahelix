package com.scottlogic.deg.generator.generation.rows;

import com.scottlogic.deg.generator.generation.GenerationConfig;

import java.util.stream.Stream;

class DummyRowSource implements RowSource
{
    private final Stream<Row> rows;

    public DummyRowSource(Row... rows) {
        this.rows = Stream.of(rows);
    }

    @Override
    public Stream<Row> generate(GenerationConfig generationConfig) {
        return this.rows;
    }
}
