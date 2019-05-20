package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.config.detail.*;
import com.scottlogic.deg.profile.v0_1.AtomicConstraintType;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public interface GenerationConfigSource  {
    File getProfileFile();
    boolean isSchemaValidationEnabled();

    boolean shouldViolate();
    List<AtomicConstraintType> getConstraintsToNotViolate();

    DataGenerationType getGenerationType();
    CombinationStrategyType getCombinationStrategyType();
    TreeWalkerType getWalkerType();
    long getMaxRows();

    OutputFormat getOutputFormat();
    Path getOutputPath();
    boolean overwriteOutputFiles();


    MonitorType getMonitorType();
    boolean shouldDoPartitioning();
    boolean isEnableTracing();
    boolean visualiseReductions();
    boolean requireFieldTyping();
    boolean dontOptimise();
}
