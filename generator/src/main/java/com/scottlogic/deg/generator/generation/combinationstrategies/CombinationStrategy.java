package com.scottlogic.deg.generator.generation.combinationstrategies;

import com.scottlogic.deg.generator.generation.databags.GeneratedObject;

import java.util.stream.Stream;

public interface CombinationStrategy {
    Stream<GeneratedObject> permute(Stream<Stream<GeneratedObject>> dataBagSequences);
}
