package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.generation.combination_strategies.ICombinationStrategy;

public class GenerationConfig {

    private final DataGenerationType dataGenerationType;
    private final TreeWalkerType walkerType;
    private final ICombinationStrategy combinationStrategy;
    private final long maxRows = 10_000_000;

    public GenerationConfig(
        DataGenerationType dataGenerationType,
        TreeWalkerType walkerType,
        ICombinationStrategy combinationStrategy) {

        this.dataGenerationType = dataGenerationType;
        this.walkerType = walkerType;
        this.combinationStrategy = combinationStrategy;
    }

    public DataGenerationType getDataGenerationType() {
        return this.dataGenerationType;
    }

    public ICombinationStrategy getCombinationStrategy() {
        return combinationStrategy;
    }

    public TreeWalkerType getWalkerType() {
        return this.walkerType;
    }

    public long getMaxRows() { return maxRows; }

    public enum DataGenerationType {
        FullSequential("full"),
        Interesting("interesting"),
        Random("random");

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
        Exhaustive("exhaustive"),
        Routed("routed");

        private final String text;

        TreeWalkerType(String text){
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }
}
