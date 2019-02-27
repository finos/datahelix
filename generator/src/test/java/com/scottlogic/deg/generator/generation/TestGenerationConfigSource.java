package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.schemas.v3.AtomicConstraintType;

import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TestGenerationConfigSource implements GenerationConfigSource {
    public GenerationConfig.DataGenerationType generationType;
    public GenerationConfig.CombinationStrategyType combinationStrategy;
    public GenerationConfig.TreeWalkerType walkerType;
    public Long maxRows;
    public boolean validateProfile = false;
    public Path outputPath;

    public TestGenerationConfigSource(
        GenerationConfig.DataGenerationType generationType,
        GenerationConfig.TreeWalkerType walkerType,
        GenerationConfig.CombinationStrategyType combinationStrategy) {
        this.generationType = generationType;
        this.combinationStrategy = combinationStrategy;
        this.walkerType = walkerType;
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
    public List<AtomicConstraintType> getConstraintsToNotViolate() {
        return Collections.emptyList();
    }

    @Override
    public GenerationConfig.MonitorType getMonitorType() {
        return GenerationConfig.MonitorType.QUIET;
    }

    @Override
    public Optional<Long> getMaxRows() {
        return maxRows == null
            ? Optional.empty()
            : Optional.of(maxRows);
    }

    public void setMaxRows(Long maxRows) {
        this.maxRows = maxRows;
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
        return outputPath;
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

    @Override
    public boolean overwriteOutputFiles() {
        return false;
    }

    @Override
    public boolean visualiseReductions() {
        return false;
    }
}
