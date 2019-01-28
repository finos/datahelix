package com.scottlogic.deg.generator.generation;

import java.io.File;
import java.nio.file.Path;

public interface GenerationConfigSource {
    GenerationConfig.DataGenerationType getGenerationType();
    GenerationConfig.CombinationStrategyType getCombinationStrategyType();
    GenerationConfig.TreeWalkerType getWalkerType();
    long getMaxRows();
    boolean getValidateProfile();
    boolean dontOptimise();
    Path getOutputPath();
    boolean isEnableTracing();
    File getProfileFile();
    boolean shouldViolate();
}
