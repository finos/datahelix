package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.ConfigSource;
import com.scottlogic.deg.generator.config.detail.*;
import com.scottlogic.deg.profile.v0_1.AtomicConstraintType;

import java.util.List;

public interface GenerationConfigSource extends ConfigSource {
    /**
     * Gets a value signifying the data generation type, i.e. one of interesting, full sequential or random.
     * @return Enum value of the current generation types.
     */
    DataGenerationType getGenerationType();

    /**
     * Gets a value signifying the combination strategy, i.e. one of exhaustive, pinning or minimal
     * @return Enum value of the current combination strategy.
     */
    CombinationStrategyType getCombinationStrategyType();

    /**
     * Gets a value signifying the current tree walker type, i.e. the reductive walker.
     * @return Enum value of the current decision tree walker.
     */
    TreeWalkerType getWalkerType();
    MonitorType getMonitorType();
    List<AtomicConstraintType> getConstraintsToNotViolate();
    long getMaxRows();
    boolean shouldDoPartitioning();
    boolean isEnableTracing();
    boolean overwriteOutputFiles();
    boolean visualiseReductions();
    boolean shouldViolate();
    boolean requireFieldTyping();

    OutputFormat getOutputFormat();
}
