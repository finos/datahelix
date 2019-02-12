package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.generation.combination_strategies.CombinationStrategy;
import com.scottlogic.deg.generator.generation.combination_strategies.ReductiveCombinationStrategy;
import com.scottlogic.deg.generator.inputs.validation.NoopProfileValidator;
import com.scottlogic.deg.generator.inputs.validation.ProfileValidator;

import java.util.Optional;

public class TestGenerationConfig extends GenerationConfig {
    public DataGenerationType dataGenerationType = DataGenerationType.INTERESTING;
    public CombinationStrategy combinationStrategy = new ReductiveCombinationStrategy();
    public TreeWalkerType walkerType = TreeWalkerType.REDUCTIVE;
    public Optional<Long> maxRows = Optional.empty();
    public ProfileValidator profileValidator = new NoopProfileValidator();

    public TestGenerationConfig() {
        super(new TestGenerationConfigSource(DataGenerationType.INTERESTING, TreeWalkerType.REDUCTIVE, CombinationStrategyType.EXHAUSTIVE));
    }

    @Override
    public DataGenerationType getDataGenerationType() {
        return dataGenerationType;
    }

    @Override
    public CombinationStrategy getCombinationStrategy() {
        return combinationStrategy;
    }

    @Override
    public TreeWalkerType getWalkerType() {
        return walkerType;
    }

    @Override
    public Optional<Long> getMaxRows() {
        return maxRows;
    }

    @Override
    public ProfileValidator getProfileValidator() {
        return profileValidator;
    }
}
