package com.scottlogic.deg.generator.generation;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.generation.combinationstrategies.*;
import com.scottlogic.deg.generator.generation.combinationstrategies.PinningCombinationStrategy;
import com.scottlogic.deg.generator.inputs.validation.NoopProfileValidator;
import com.scottlogic.deg.generator.inputs.validation.ProfileValidator;
import com.scottlogic.deg.generator.inputs.validation.ReportingProfileValidator;
import com.scottlogic.deg.generator.inputs.validation.reporters.SystemOutProfileValidationReporter;

import java.util.Optional;

public class GenerationConfig {

    private final DataGenerationType dataGenerationType;
    private final TreeWalkerType walkerType;
    private final CombinationStrategyType combinationStrategy;
    private final Optional<Long> maxRows;
    private final boolean validateProfile;

    @Inject
    public GenerationConfig(GenerationConfigSource source) {
        this.dataGenerationType = source.getGenerationType();
        this.walkerType = source.getWalkerType();
        this.combinationStrategy = source.getCombinationStrategyType();
        this.maxRows = source.getMaxRows();
        this.validateProfile = source.getValidateProfile();
    }

    public DataGenerationType getDataGenerationType() {
        return this.dataGenerationType;
    }

    public CombinationStrategy getCombinationStrategy() {
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

    public Optional<Long> getMaxRows() { return maxRows; }

    public ProfileValidator getProfileValidator() {

        if(validateProfile) {
            return new ReportingProfileValidator(new SystemOutProfileValidationReporter());
        }

        return new NoopProfileValidator();
    }

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

    public enum TreeWalkerType {
        CARTESIAN_PRODUCT(Constants.WalkerTypes.CARTESIAN_PRODUCT),
        ROUTED(Constants.WalkerTypes.ROUTED),
        REDUCTIVE(Constants.WalkerTypes.REDUCTIVE);

        private final String text;

        TreeWalkerType(String text){
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

        public static final long DEFAULT_MAX_ROWS = 10_000_000;
    }
}
