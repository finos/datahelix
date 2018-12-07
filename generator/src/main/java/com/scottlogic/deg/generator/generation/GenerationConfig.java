package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.Generate;
import com.scottlogic.deg.generator.generation.combination_strategies.*;

public class GenerationConfig {

    private final DataGenerationType dataGenerationType;
    private final TreeWalkerType walkerType;
    private final CombinationStrategyType combinationStrategy;
    private final long maxRows = 10_000_000;

    public GenerationConfig(
        DataGenerationType dataGenerationType,
        TreeWalkerType walkerType,
        CombinationStrategyType combinationStrategy) {

        this.dataGenerationType = dataGenerationType;
        this.walkerType = walkerType;
        this.combinationStrategy = combinationStrategy;
    }

    public DataGenerationType getDataGenerationType() {
        return this.dataGenerationType;
    }

    public ICombinationStrategy getCombinationStrategy() {
        if (this.walkerType == TreeWalkerType.REDUCTIVE){
            return new ReductiveCombinationStrategy();
        }

        switch(this.combinationStrategy){
            case EXHAUSTIVE: return new ExhaustiveCombinationStrategy();
            case PINNING: return new PinningCombinationStrategy();
            case MINIMAL: return new MinimalCombinationStrategy();
            default:
                throw new UnsupportedOperationException(
                    "$Combination strategy {this.combinationStrategy} is unsupported.");
        }
    }

    public TreeWalkerType getWalkerType() {
        return this.walkerType;
    }

    public long getMaxRows() { return maxRows; }

    public enum DataGenerationType {
        FULL_SEQUENTIAL("full"),
        INTERESTING("interesting"),
        RANDOM("random");

        private final String text;

        DataGenerationType(String text){
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum TreeWalkerType {
        CARTESIAN_PRODUCT(Generate.cartestian_product_walker_type),
        ROUTED("routed"),
        REDUCTIVE("reductive");

        private final String text;

        TreeWalkerType(String text){
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum CombinationStrategyType {

        EXHAUSTIVE("exhaustive"),
        PINNING("pinning"),
        MINIMAL("minimal");
        private final String text;

        CombinationStrategyType(String text){
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

}
