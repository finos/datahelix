package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.config.detail.*;

import java.nio.file.Path;

public interface GenerationConfigSource  {
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
