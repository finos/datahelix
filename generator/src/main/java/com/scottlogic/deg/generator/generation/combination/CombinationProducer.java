package com.scottlogic.deg.generator.generation.combination;

import com.scottlogic.deg.generator.decisiontree.DecisionTree;

import java.util.stream.Stream;

public interface CombinationProducer {
    Stream<Combination> getCombinations(DecisionTree tree);
}
