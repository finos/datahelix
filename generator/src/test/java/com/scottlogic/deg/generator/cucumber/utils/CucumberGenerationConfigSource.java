package com.scottlogic.deg.generator.cucumber.utils;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.schemas.v3.AtomicConstraintType;

import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class CucumberGenerationConfigSource implements GenerationConfigSource {

    private final CucumberTestState state;

    @Inject
    public CucumberGenerationConfigSource(CucumberTestState state) {
        this.state = state;
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
        return Collections.emptyList();
    }

    @Override
    public long getMaxRows() {
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
        return null;
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
    public boolean shouldViolate() {
        return state.shouldViolate;
    }

    @Override
    public boolean getVerbose() {
        return false;
    }

    @Override
    public boolean getQuiet() {
        return state.quiet;
    }
}
