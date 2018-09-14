package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.generation.combination_strategies.ICombinationStrategy;

public class GenerationConfig {
    public enum DataGenerationType {
        FullSequential,
        Interesting,
        Random
    }

    private final DataGenerationType dataGenerationType;
    private final ICombinationStrategy combinationStrategy;

    public GenerationConfig(
        DataGenerationType dataGenerationType,
        ICombinationStrategy combinationStrategy) {

        this.dataGenerationType = dataGenerationType;
        this.combinationStrategy = combinationStrategy;
    }

    public DataGenerationType getDataGenerationType() {
        return this.dataGenerationType;
    }

    public ICombinationStrategy getCombinationStrategy() {
        return combinationStrategy;
    }
}
