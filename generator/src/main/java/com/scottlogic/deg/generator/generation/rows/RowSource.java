package com.scottlogic.deg.generator.generation.rows;

import com.scottlogic.deg.generator.generation.GenerationConfig;

import java.util.stream.Stream;

public interface RowSource {
    Stream<Row> generate(GenerationConfig generationConfig);
}
