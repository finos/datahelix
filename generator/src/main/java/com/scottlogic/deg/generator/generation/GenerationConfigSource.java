package com.scottlogic.deg.generator.generation;

public interface GenerationConfigSource {
    GenerationConfig.DataGenerationType getGenerationType();
    GenerationConfig.CombinationStrategyType getCombinationStrategyType();
    GenerationConfig.TreeWalkerType getWalkerType();
    long getMaxRows();
}
