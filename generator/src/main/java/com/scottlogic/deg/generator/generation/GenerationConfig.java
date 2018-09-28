package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.generation.combination_strategies.ICombinationStrategy;

import java.nio.file.Path;

public class GenerationConfig {

    private final DataGenerationType dataGenerationType;
    private final ICombinationStrategy combinationStrategy;
    private final long maxRows = 10_000_000;

    private Path debugPath;

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

    public long getMaxRows() { return maxRows; }

    public Path getDebugPath() {
        return debugPath;
    }

    public void setDebugPath(Path debugPath) {
        this.debugPath = debugPath;
    }

    public enum DataGenerationType {
        FullSequential,
        Interesting,
        Random
    }
}
