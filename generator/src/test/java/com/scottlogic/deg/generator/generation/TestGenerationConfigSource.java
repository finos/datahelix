package com.scottlogic.deg.generator.generation;

import java.io.File;
import java.nio.file.Path;

public class TestGenerationConfigSource implements GenerationConfigSource {
    public GenerationConfig.DataGenerationType generationType;
    public GenerationConfig.CombinationStrategyType combinationStrategy;
    public GenerationConfig.TreeWalkerType walkerType;
    public long maxRows = GenerationConfig.Constants.DEFAULT_MAX_ROWS;
    public boolean validateProfile = false;

    public TestGenerationConfigSource(
        GenerationConfig.DataGenerationType generationType,
        GenerationConfig.TreeWalkerType walkerType,
        GenerationConfig.CombinationStrategyType combinationStrategy) {
        this.generationType = generationType;
        this.combinationStrategy = combinationStrategy;
        this.walkerType = walkerType;
    }

    public TestGenerationConfigSource() {
    }

    @Override
    public GenerationConfig.DataGenerationType getGenerationType() {
        return this.generationType;
    }

    @Override
    public GenerationConfig.CombinationStrategyType getCombinationStrategyType() {
        return this.combinationStrategy;
    }

    @Override
    public GenerationConfig.TreeWalkerType getWalkerType() {
        return this.walkerType;
    }

    @Override
    public long getMaxRows() {
        return this.maxRows;
    }

    @Override
    public boolean getValidateProfile() {
        return validateProfile;
    }

    @Override
    public boolean shouldDoPartitioning() {
        return true;
    }

    @Override
    public boolean dontOptimise() {
        return false;
    }

    @Override
    public Path getOutputPath() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEnableTracing() {
        return false;
    }

    @Override
    public File getProfileFile() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean shouldViolate() {
        return false;
    }
}
