package com.scottlogic.deg.generator.generation;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.generation.combinationstrategies.*;

import java.math.BigDecimal;
import java.util.Optional;

public class GenerationConfig {

    private final DataGenerationType dataGenerationType;
    private final TreeWalkerType walkerType;
    private final CombinationStrategyType combinationStrategyType;
    private final long maxRows;

    @Inject
    public GenerationConfig(GenerationConfigSource source) {
        this.dataGenerationType = source.getGenerationType();
        this.walkerType = source.getWalkerType();
        this.combinationStrategyType = source.getCombinationStrategyType();
        this.maxRows = source.getMaxRows();
    }

    public DataGenerationType getDataGenerationType() {
        return this.dataGenerationType;
    }


    public TreeWalkerType getWalkerType() {
        return this.walkerType;
    }

    public long getMaxRows() { return maxRows; }

    public CombinationStrategyType getCombinationStrategyType() {
        return combinationStrategyType;
    }

    public enum DataGenerationType {
        FULL_SEQUENTIAL,
        INTERESTING,
        RANDOM
    }

    public enum TreeWalkerType {
        CARTESIAN_PRODUCT,
        REDUCTIVE
    }

    public enum MonitorType {
        VERBOSE,
        QUIET,
        STANDARD
    }

    public enum CombinationStrategyType {
        EXHAUSTIVE,
        PINNING,
        MINIMAL
    }

    public enum OutputFormat {
        CSV,
        JSON
    }

    public static class Constants {

        public static final long DEFAULT_MAX_ROWS = 1000;

        public static final BigDecimal NUMERIC_MAX = new BigDecimal("1e20");
        public static final BigDecimal NUMERIC_MIN = new BigDecimal("-1e20");
        public static final int MAX_STRING_LENGTH = 1000;
    }
}
