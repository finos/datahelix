package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.config.detail.*;

import java.nio.file.Path;

public interface GenerationConfigSource  {
    DataGenerationType getGenerationType();
    CombinationStrategyType getCombinationStrategyType();
    TreeWalkerType getWalkerType();
    long getMaxRows();

    MonitorType getMonitorType();
    boolean shouldDoPartitioning();
    boolean visualiseReductions();
    boolean requireFieldTyping();
    boolean dontOptimise();
}
