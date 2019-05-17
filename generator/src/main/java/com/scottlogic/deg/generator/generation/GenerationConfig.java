package com.scottlogic.deg.generator.generation;

import java.math.BigDecimal;

public class GenerationConfig {

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
