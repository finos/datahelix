package com.scottlogic.deg.generator.commandline;

import com.scottlogic.deg.generator.GenerateExecute;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.schemas.v0_1.AtomicConstraintType;
import picocli.CommandLine;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

/**
 * This class holds the generate specific command line options.
 */
@picocli.CommandLine.Command(
    name = "generate",
    description = "Produces data using any options provided.",
    descriptionHeading = "%nDescription:%n",
    parameterListHeading = "%nParameters:%n",
    optionListHeading = "%nOptions:%n",
    abbreviateSynopsis = true)
public class GenerateCommandLine extends CommandLineBase implements GenerationConfigSource {

    @CommandLine.Parameters(index = "1", description = "The path to write the generated data file to.")
    private Path outputPath;

    @CommandLine.Option(names = {"-t", "--generation-type"},
        description = "Determines the type of data generation performed (" +
            GenerationConfig.Constants.GenerationTypes.FULL_SEQUENTIAL +
            ", " + GenerationConfig.Constants.GenerationTypes.INTERESTING +
            ", " + GenerationConfig.Constants.GenerationTypes.RANDOM + ").",
        defaultValue = GenerationConfig.Constants.GenerationTypes.DEFAULT,
        hidden = true)
    private GenerationConfig.DataGenerationType generationType;

    @CommandLine.Option(names = {"-c", "--combination-strategy"},
        description = "Determines the type of combination strategy used (" +
            GenerationConfig.Constants.CombinationStrategies.PINNING + ", " +
            GenerationConfig.Constants.CombinationStrategies.EXHAUSTIVE + ", " +
            GenerationConfig.Constants.CombinationStrategies.MINIMAL + ").",
        defaultValue = GenerationConfig.Constants.CombinationStrategies.DEFAULT,
        hidden = true)
    @SuppressWarnings("unused")
    private GenerationConfig.CombinationStrategyType combinationType;

    @CommandLine.Option(
        names = {"--no-partition"},
        description = "Prevents tree partitioning",
        hidden = true)
    private boolean dontPartitionTrees;

    @CommandLine.Option(names = {"-w", "--walker-type"},
        description = "Determines the tree walker that should be used.",
        defaultValue = GenerationConfig.Constants.WalkerTypes.DEFAULT,
        hidden = true)
    private GenerationConfig.TreeWalkerType walkerType;

    @CommandLine.Option(
        names = {"-n", "--max-rows"},
        description = "Defines the maximum number of rows that should be generated")
    private Long maxRows;

    @CommandLine.Option(
        names = {"--validate-profile"},
        description = "Defines whether to validate the profile (" +
            true+ ", " +
            false + ").")
    private boolean validateProfile;

    @CommandLine.Option(
        names = {"--trace-constraints"},
        description = "Defines whether constraint tracing is enabled for the output",
        hidden = true)
    private boolean enableTracing;

    @CommandLine.Option(
        names = {"--violate"},
        description = "Defines whether to generate violating data")
    private boolean violateProfile;

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
        description = "Visualise each tree reduction",
        hidden = true)
    private Boolean visualiseReductions = false;

    @CommandLine.Option(
        names = {"-o"},
        description = "Output format",
        defaultValue = GenerationConfig.Constants.OutputFormats.DEFAULT)
    private GenerationConfig.OutputFormat outputFormat;

    @CommandLine.Option(
        names = {"--allow-untyped-fields"},
        description = "Remove the need for each field to have at least one compliant typing constraint applied")
    private boolean allowUntypedFields = false;

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
    public boolean isSchemaValidationEnabled() {
        return this.enableSchemaValidation;
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
    public boolean requireFieldTyping(){
        return !allowUntypedFields;
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

    public GenerationConfig.OutputFormat getOutputFormat() {
        return outputFormat;
    }

    @Override
    protected Class<? extends Runnable> getExecutorType() {
        return GenerateExecute.class;
    }
}
