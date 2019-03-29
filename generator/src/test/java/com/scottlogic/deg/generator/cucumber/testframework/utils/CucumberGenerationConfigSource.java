package com.scottlogic.deg.generator.cucumber.testframework.utils;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.schemas.v0_1.AtomicConstraintType;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class CucumberGenerationConfigSource implements GenerationConfigSource {
    private final CucumberTestState state;

    @Inject
    public CucumberGenerationConfigSource(CucumberTestState state) {
        this.state = state;
    }

    @Override
    public boolean requireFieldTyping() {
        return state.requireFieldTyping;
    }

    @Override
    public GenerationConfig.DataGenerationType getGenerationType() {
        return state.dataGenerationType;
    }

    @Override
    public GenerationConfig.CombinationStrategyType getCombinationStrategyType() {
        return state.combinationStrategyType;
    }

    @Override
    public GenerationConfig.TreeWalkerType getWalkerType() {
        return state.walkerType;
    }

    @Override
    public List<AtomicConstraintType> getConstraintsToNotViolate() {
        return state.getConstraintsToNotViolate();
    }

    @Override
    public GenerationConfig.MonitorType getMonitorType() {
        return GenerationConfig.MonitorType.QUIET;
    }

    @Override
    public Optional<Long> getMaxRows() {
        return state.maxRows;
    }

    @Override
    public boolean getValidateProfile() {
        return false;
    }

    @Override
    public boolean shouldDoPartitioning() {
        return false;
    }

    @Override
    public boolean dontOptimise() {
        return false;
    }

    @Override
    public Path getOutputPath() {
        return new File("mockFilePath").toPath();
    }

    @Override
    public boolean isEnableTracing() {
        return false;
    }

    @Override
    public File getProfileFile() {
        return new File("mockFilePath");
    }

    @Override
    public boolean isSchemaValidationEnabled() {
        return false;
    }

    @Override
    public boolean shouldViolate() {
        return state.shouldViolate;
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
    public GenerationConfig.OutputFormat getOutputFormat() {
        return null;
    }
}
