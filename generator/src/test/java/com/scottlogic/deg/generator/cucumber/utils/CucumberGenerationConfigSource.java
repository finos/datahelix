package com.scottlogic.deg.generator.cucumber.utils;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;

import java.io.File;
import java.nio.file.Path;

public class CucumberGenerationConfigSource implements GenerationConfigSource {

    private TestState state;

    @Inject
    public CucumberGenerationConfigSource(TestState state) {
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
}
