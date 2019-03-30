package com.scottlogic.deg.generator.generation;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.generation.combinationstrategies.*;

import java.math.BigDecimal;
import java.util.Optional;

public class GenerationConfig {

    private final DataGenerationType dataGenerationType;
    private final CombinationStrategyType combinationStrategy;
    private final Optional<Long> maxRows;

    @Inject
    public GenerationConfig(GenerationConfigSource source) {
        this.dataGenerationType = source.getGenerationType();
        this.combinationStrategy = source.getCombinationStrategyType();
        this.maxRows = source.getMaxRows();
    }

    public DataGenerationType getDataGenerationType() {
        return this.dataGenerationType;
    }

    public CombinationStrategy getCombinationStrategy() {
        return new ReductiveCombinationStrategy();
    }

    public Optional<Long> getMaxRows() { return maxRows; }

    public enum DataGenerationType {
        FULL_SEQUENTIAL(Constants.GenerationTypes.FULL_SEQUENTIAL),
        INTERESTING(Constants.GenerationTypes.INTERESTING),
        RANDOM(Constants.GenerationTypes.RANDOM);

        private final String text;

        DataGenerationType(String text){
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum MonitorType {
        VERBOSE(Constants.MonitorTypes.VERBOSE),
        QUIET(Constants.MonitorTypes.QUIET),
        STANDARD(Constants.MonitorTypes.STANDARD);

        private final String text;

        MonitorType(String text){
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum CombinationStrategyType {

        EXHAUSTIVE(Constants.CombinationStrategies.EXHAUSTIVE),
        PINNING(Constants.CombinationStrategies.PINNING),
        MINIMAL(Constants.CombinationStrategies.MINIMAL);
        private final String text;

        CombinationStrategyType(String text){
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum OutputFormat {
        CSV(Constants.OutputFormats.CSV),
        JSON(Constants.OutputFormats.JSON);
        private final String text;

        OutputFormat(String text){
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public static class Constants {
        public static class WalkerTypes {
            public static final String CARTESIAN_PRODUCT = "CARTESIAN_PRODUCT";
            public static final String ROUTED = "ROUTED";
            public static final String REDUCTIVE = "REDUCTIVE";

            public static final String DEFAULT = REDUCTIVE;
        }

        public static class CombinationStrategies {
            public static final String EXHAUSTIVE = "EXHAUSTIVE";
            public static final String PINNING = "PINNING";
            public static final String MINIMAL = "MINIMAL";

            public static final String DEFAULT = PINNING;
        }

        public static class GenerationTypes {
            public static final String FULL_SEQUENTIAL = "FULL_SEQUENTIAL";
            public static final String INTERESTING = "INTERESTING";
            public static final String RANDOM = "RANDOM";

            public static final String DEFAULT = RANDOM;
        }

        public static class MonitorTypes {
            public static final String QUIET = "QUIET";
            public static final String VERBOSE = "VERBOSE";
            public static final String STANDARD = "STANDARD";
        }

        public static class OutputFormats {
            public static final String CSV = "CSV";
            public static final String JSON = "JSON";

            public static final String DEFAULT = CSV;
        }

        public static final long DEFAULT_MAX_ROWS = 1000;

        public static final BigDecimal NUMERIC_MAX = new BigDecimal("1e20");
        public static final BigDecimal NUMERIC_MIN = new BigDecimal("-1e20");
    }
}
