package com.scottlogic.deg.generator.generation.combinationstrategies;

import com.scottlogic.deg.generator.generation.rows.Row;

import java.util.stream.Stream;

public interface CombinationStrategy {
    Stream<Row> permute(Stream<Stream<Row>> dataBagSequences);
}
