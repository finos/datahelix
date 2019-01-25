package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.schemas.v3.AtomicConstraintType;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public interface GenerationConfigSource {
    GenerationConfig.DataGenerationType getGenerationType();
    GenerationConfig.CombinationStrategyType getCombinationStrategyType();
    GenerationConfig.TreeWalkerType getWalkerType();
    List<AtomicConstraintType> getConstraintsToNotViolate();
    long getMaxRows();
    boolean getValidateProfile();
    boolean shouldDoPartitioning();
    boolean dontOptimise();
    Path getOutputPath();
    boolean isEnableTracing();
    File getProfileFile();
    boolean shouldViolate();
}
