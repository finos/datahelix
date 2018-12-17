package com.scottlogic.deg.generator.generation.combination;

import java.util.stream.Stream;

public interface CombinationProducer {
    Stream<Combination> getCombinations();
}
