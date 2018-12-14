package com.scottlogic.deg.generator.generation.combination_strategies;

import com.scottlogic.deg.generator.generation.databags.DataBag;

import java.util.stream.Stream;

public interface CombinationStrategy {
    Stream<DataBag> permute(Stream<Stream<DataBag>> dataBagSequences);
}
