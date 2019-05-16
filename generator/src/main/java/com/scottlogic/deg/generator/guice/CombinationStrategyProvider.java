package com.scottlogic.deg.generator.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.generation.combinationstrategies.*;

public class CombinationStrategyProvider  implements Provider<CombinationStrategy> {
    private final GenerationConfigSource config;

    @Inject
    public CombinationStrategyProvider(GenerationConfigSource config){
        this.config = config;
    }

    @Override
    public CombinationStrategy get() {
        if (config.getDataGenerationType() == GenerationConfig.DataGenerationType.RANDOM){
            // The minimal combination strategy doesn't reuse values for fields.
            // This is required to get truly random data.
            return new MinimalCombinationStrategy();
        }

        if (config.getWalkerType() == GenerationConfig.TreeWalkerType.REDUCTIVE){
            return new ReductiveCombinationStrategy();
        }

        switch(config.getCombinationStrategyType()){
            case EXHAUSTIVE: return new ExhaustiveCombinationStrategy();
            case PINNING: return new PinningCombinationStrategy();
            case MINIMAL: return new MinimalCombinationStrategy();
            default:
                throw new UnsupportedOperationException(
                    "$Combination strategy {this.combinationStrategy} is unsupported.");
        }
    }
}
