package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.generation.combination_strategies.FieldExhaustiveCombinationStrategy;
import com.scottlogic.deg.generator.generation.combination_strategies.ICombinationStrategy;

public class GenerationConfig {
    public static GenerationConfig exhaustivePresets = new GenerationConfig(
        true,
        new FieldExhaustiveCombinationStrategy());

    private final boolean shouldEnumerateSetsExhaustively;
    private final ICombinationStrategy combinationStrategy;

    public GenerationConfig(
        boolean shouldEnumerateSetsExhaustively,
        ICombinationStrategy combinationStrategy) {

        this.shouldEnumerateSetsExhaustively = shouldEnumerateSetsExhaustively;
        this.combinationStrategy = combinationStrategy;
    }

    public boolean shouldEnumerateSetsExhaustively() {
        return this.shouldEnumerateSetsExhaustively;
    }

    public ICombinationStrategy getCombinationStrategy() {
        return combinationStrategy;
    }
}
