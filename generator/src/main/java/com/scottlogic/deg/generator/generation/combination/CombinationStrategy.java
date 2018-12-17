package com.scottlogic.deg.generator.generation.combination;

import com.scottlogic.deg.generator.Field;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public interface CombinationStrategy {
    List<Combination> getCombinations(Map<Field, Stream<Object>> generatedData);
}
