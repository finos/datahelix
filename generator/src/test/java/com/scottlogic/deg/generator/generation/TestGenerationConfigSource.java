package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.config.detail.*;
import com.scottlogic.deg.profile.v0_1.AtomicConstraintType;

import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class TestGenerationConfigSource implements GenerationConfigSource {
    public DataGenerationType generationType;
    public CombinationStrategyType combinationStrategy;
    public TreeWalkerType walkerType;
    public long maxRows;
    public boolean validateProfile = false;
    public Path outputPath;
    public OutputFormat outputFormat = OutputFormat.CSV;
    public boolean requireFieldTyping = true;

    public TestGenerationConfigSource(
        DataGenerationType generationType,
        TreeWalkerType walkerType,
        CombinationStrategyType combinationStrategy) {
        this.generationType = generationType;
        this.combinationStrategy = combinationStrategy;
        this.walkerType = walkerType;
    }

    @Override
    public boolean requireFieldTyping() {
        return requireFieldTyping;
    }

    @Override
    public DataGenerationType getGenerationType() {
        return this.generationType;
    }

    @Override
    public CombinationStrategyType getCombinationStrategyType() {
        return this.combinationStrategy;
    }

    @Override
    public TreeWalkerType getWalkerType() {
        return this.walkerType;
    }

    @Override
    public List<AtomicConstraintType> getConstraintsToNotViolate() {
        return Collections.emptyList();
    }

    @Override
    public MonitorType getMonitorType() {
        return MonitorType.QUIET;
    }

    @Override
    public long getMaxRows() {
        return maxRows;
    }

    public void setMaxRows(long maxRows) {
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
    public boolean isSchemaValidationEnabled() {
        return true;
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

    @Override
    public OutputFormat getOutputFormat() {
        return outputFormat;
    }
}
