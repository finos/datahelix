package com.scottlogic.deg.generator.CommandLine;

import com.scottlogic.deg.generator.GenerateTestCasesExecute;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import picocli.CommandLine;

import java.io.File;
import java.nio.file.Path;

public class GenerateTestCasesCommandLine extends CommandLineBase implements GenerationConfigSource {
    @CommandLine.Parameters(index = "0", description = "The path of the profile json file.")
    @SuppressWarnings("unused")
    private File profileFile;

    @CommandLine.Parameters(index = "1", description = "The directory into which generated data should be saved.")
    @SuppressWarnings("unused")
    private Path outputDir;

    @CommandLine.Option(names = {"-t", "--t", "--generation-type"},
        description = "Determines the type of data generation performed (" +
            GenerationConfig.Constants.GenerationTypes.FULL_SEQUENTIAL +
            ", " + GenerationConfig.Constants.GenerationTypes.INTERESTING +
            ", " + GenerationConfig.Constants.GenerationTypes.RANDOM + ").",
        defaultValue = GenerationConfig.Constants.GenerationTypes.DEFAULT)
    @SuppressWarnings("unused")
    private GenerationConfig.DataGenerationType generationType;

    @CommandLine.Option(names = {"-c", "--c", "--combination-strategy"},
        description = "Determines the type of combination strategy used (" +
            GenerationConfig.Constants.CombinationStrategies.PINNING + ", " +
            GenerationConfig.Constants.CombinationStrategies.EXHAUSTIVE + ", " +
            GenerationConfig.Constants.CombinationStrategies.MINIMAL + ").",
        defaultValue = GenerationConfig.Constants.CombinationStrategies.DEFAULT)
    @SuppressWarnings("unused")
    private GenerationConfig.CombinationStrategyType combinationType;

    @CommandLine.Option(names = {"-w", "--w", "--walker-type"},
        description = "Determines the tree walker that should be used.",
        defaultValue = GenerationConfig.Constants.WalkerTypes.DEFAULT,
        hidden = true)
    @SuppressWarnings("unused")
    private GenerationConfig.TreeWalkerType walkerType;

    @CommandLine.Option(
        names = {"--no-optimise"},
        description = "Prevents tree optimisation",
        hidden = true)
    @SuppressWarnings("unused")
    private boolean dontOptimise;

    @CommandLine.Option(
        names = {"--no-partition"},
        description = "Prevents tree partitioning",
        hidden = true)
    @SuppressWarnings("unused")
    private boolean dontPartitionTrees;

    @CommandLine.Option(
        names = {"-n", "--n", "--max-rows"},
        description = "Defines the maximum number of rows that should be generated")
    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private long maxRows = GenerationConfig.Constants.DEFAULT_MAX_ROWS;

    @CommandLine.Option(
        names = {"-v", "--v", "--validate-profile"},
        description = "Defines whether to validate the profile (" +
            true+ ", " +
            false + ").")
    private boolean validateProfile;

    @CommandLine.Option(
        names = {"--trace-constraints"},
        description = "Defines whether constraint tracing is enabled for the output")
    private boolean enableTracing;

    @Override
    protected Class<? extends Runnable> getExecutorType() {
        return GenerateTestCasesExecute.class;
    }

    @Override
    public GenerationConfig.DataGenerationType getGenerationType() {
        return this.generationType;
    }

    @Override
    public GenerationConfig.CombinationStrategyType getCombinationStrategyType() {
        return this.combinationType;
    }

    @Override
    public GenerationConfig.TreeWalkerType getWalkerType() {
        return this.walkerType;
    }

    @Override
    public long getMaxRows() {
        return this.maxRows;
    }

    @Override
    public boolean getValidateProfile() {
        return this.validateProfile;
    }

    @Override
    public File getProfileFile() {
        return this.profileFile;
    }

    @Override
    public boolean shouldDoPartitioning() {
        return !this.dontPartitionTrees;
    }

    @Override
    public boolean dontOptimise() {
        return this.dontOptimise;
    }

    @Override
    public Path getOutputPath() {
        return this.outputDir;
    }

    @Override
    public boolean isEnableTracing() {
        return this.enableTracing;
    }
}
