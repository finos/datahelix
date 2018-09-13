package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.generation.combination_strategies.FieldExhaustiveCombinationStrategy;
import com.scottlogic.deg.generator.generation.combination_strategies.ICombinationStrategy;

public class GenerationConfig {
    public static GenerationConfig exhaustivePresets = new GenerationConfig(
        DataGenerationType.Interesting,
        false,
        new FieldExhaustiveCombinationStrategy());

    public enum DataGenerationType
    {
        FullSequential,
        Interesting,
        Random
    }

    private final boolean shouldChooseFiniteSampling;
    private final DataGenerationType dataGenerationType;
    private final ICombinationStrategy combinationStrategy;

    public GenerationConfig(
        DataGenerationType dataGenerationType,
        boolean shouldChooseFiniteSampling,
        ICombinationStrategy combinationStrategy) {

        this.dataGenerationType = dataGenerationType;
        this.shouldChooseFiniteSampling = shouldChooseFiniteSampling;
        this.combinationStrategy = combinationStrategy;
    }

    public boolean shouldChooseFiniteSampling() {
        return this.shouldChooseFiniteSampling;
    }

    public DataGenerationType getDataGenerationType() {
        return this.dataGenerationType;
    }

    public ICombinationStrategy getCombinationStrategy() {
        return combinationStrategy;
    }
}
