package com.scottlogic.deg.generator.generation.combination;

import com.scottlogic.deg.generator.decisiontree.rule_strategy.Combination;

import java.util.stream.Stream;

public interface CombinationProducer {
    Stream<Combination> getCombinations();
}
