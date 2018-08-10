package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.generation.combination_strategies.FieldExhaustiveCombinationStrategy;
import com.scottlogic.deg.generator.generation.combination_strategies.ICombinationStrategy;

public class GenerationConfig {
    public static GenerationConfig exhaustivePresets = new GenerationConfig(
        true,
        true,
        new FieldExhaustiveCombinationStrategy());

    private final boolean shouldEnumerateSetsExhaustively;
    private final boolean shouldChooseFiniteSampling;
    private final ICombinationStrategy combinationStrategy;

    public GenerationConfig(
        boolean shouldEnumerateSetsExhaustively,
        boolean shouldChooseFiniteSampling,
        ICombinationStrategy combinationStrategy) {

        this.shouldEnumerateSetsExhaustively = shouldEnumerateSetsExhaustively;
        this.shouldChooseFiniteSampling = shouldChooseFiniteSampling;
        this.combinationStrategy = combinationStrategy;
    }

    public boolean shouldEnumerateSetsExhaustively() {
        return this.shouldEnumerateSetsExhaustively;
    }

    public boolean shouldChooseFiniteSampling() {
        return this.shouldChooseFiniteSampling;
    }

    public ICombinationStrategy getCombinationStrategy() {
        return combinationStrategy;
    }
}
