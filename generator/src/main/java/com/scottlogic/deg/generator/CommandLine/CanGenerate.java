package com.scottlogic.deg.generator.CommandLine;

import com.scottlogic.deg.generator.generation.GenerationConfig;

public interface CanGenerate{
    GenerationConfig.DataGenerationType getGenerationType();
    GenerationConfig.CombinationStrategyType getCombinationType();
    GenerationConfig.TreeWalkerType getWalkerType();
}
