package com.scottlogic.deg.generator.CommandLine;

import com.scottlogic.deg.generator.GenerateExecute;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.schemas.v3.AtomicConstraintType;
import picocli.CommandLine;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@picocli.CommandLine.Command(
    name = "generate",
    description = "Produces data using any options provided.",
    mixinStandardHelpOptions = true,
    version = "1.0")
public class GenerateCommandLine extends CommandLineBase {

    @CommandLine.Parameters(index = "0", description = "The path of the profile json file.")
    private File profileFile;

    @CommandLine.Parameters(index = "1", description = "The path to write the generated data file to.")
    private Path outputPath;

    @CommandLine.Option(names = {"-t", "--t", "--generation-type"},
        description = "Determines the type of data generation performed (" +
            GenerationConfig.Constants.GenerationTypes.FULL_SEQUENTIAL +
            ", " + GenerationConfig.Constants.GenerationTypes.INTERESTING +
            ", " + GenerationConfig.Constants.GenerationTypes.RANDOM + ").",
        defaultValue = GenerationConfig.Constants.GenerationTypes.DEFAULT)
    private GenerationConfig.DataGenerationType generationType;

    @CommandLine.Option(names = {"-c", "--c", "--combination-strategy"},
        description = "Determines the type of combination strategy used (" +
            GenerationConfig.Constants.CombinationStrategies.PINNING + ", " +
            GenerationConfig.Constants.CombinationStrategies.EXHAUSTIVE + ", " +
            GenerationConfig.Constants.CombinationStrategies.MINIMAL + ").",
        defaultValue = GenerationConfig.Constants.CombinationStrategies.DEFAULT)
    @SuppressWarnings("unused")
    private GenerationConfig.CombinationStrategyType combinationType;

    @CommandLine.Option(
        names = {"--no-optimise"},
        description = "Prevents tree optimisation",
        hidden = true)
    private boolean dontOptimise;

    @CommandLine.Option(
        names = {"--no-partition"},
        description = "Prevents tree partitioning",
        hidden = true)
    private boolean dontPartitionTrees;

    @CommandLine.Option(names = {"-w", "--w", "--walker-type"},
        description = "Determines the tree walker that should be used.",
        defaultValue = GenerationConfig.Constants.WalkerTypes.DEFAULT,
        hidden = true)
    private GenerationConfig.TreeWalkerType walkerType;

    @CommandLine.Option(
        names = {"-n", "--n", "--max-rows"},
        description = "Defines the maximum number of rows that should be generated")
    private Long maxRows;

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

    @CommandLine.Option(
        names = {"--violate"},
        description = "Defines whether to generate violating data")
    private boolean violateProfile;

    @CommandLine.Option(
        names = {"--overwrite"},
        description = "Defines whether to overwrite existing output files")
    private boolean overwriteOutputFiles;

    @CommandLine.Option(
        names = {"--dont-violate"},
        arity = "0..",
        description = "Choose types of constraint should not be violated")
    private List<AtomicConstraintType> constraintsToNotViolate;

    @CommandLine.Option(
        names = {"--quiet"},
        description = "Turns OFF default monitoring")
    private Boolean quiet = false;

    @CommandLine.Option(
        names = {"--verbose"},
        description = "Turns ON system out monitoring")
    private Boolean verbose = false;

    @CommandLine.Option(
        names = {"--visualise-reductions"},
        description = "Visualise each tree reduction")
    private Boolean visualiseReductions = false;

    @Override
    public boolean shouldDoPartitioning() {
        return !this.dontPartitionTrees;
    }

    @Override
    public boolean dontOptimise() {
        return this.dontOptimise;
    }

    @Override
    public File getProfileFile() {
        return this.profileFile;
    }

    @Override
    public boolean shouldViolate() {
        return this.violateProfile;
    }

    @Override
    public boolean overwriteOutputFiles() {
        return this.overwriteOutputFiles;
    }

    @Override
    public Path getOutputPath() {
        return this.outputPath;
    }

    @Override
    public boolean isEnableTracing() {
        return this.enableTracing;
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
    public List<AtomicConstraintType> getConstraintsToNotViolate() {
        return constraintsToNotViolate;
    }

    @Override
    public GenerationConfig.MonitorType getMonitorType() {
        if (this.verbose) {
            return GenerationConfig.MonitorType.VERBOSE;
        }
        if (this.quiet) {
            return GenerationConfig.MonitorType.QUIET;
        }
        return GenerationConfig.MonitorType.STANDARD;
    }

    @Override
    public Optional<Long> getMaxRows() {
        return maxRows == null
            ? Optional.empty()
            : Optional.of(maxRows);
    }

    @Override
    public boolean getValidateProfile() {
        return this.validateProfile;
    }

    @Override
    public boolean visualiseReductions() {
        return visualiseReductions;
    }

    @Override
    protected Class<? extends Runnable> getExecutorType() {
        return GenerateExecute.class;
    }
}
