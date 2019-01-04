package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.analysis.FieldDependencyAnalyser;
import com.scottlogic.deg.generator.decisiontree.MostProlificConstraintOptimiser;
import com.scottlogic.deg.generator.decisiontree.NoopDecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.ProfileDecisionTreeFactory;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.NoopTreePartitioner;
import com.scottlogic.deg.generator.generation.*;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.RelatedFieldTreePartitioner;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.generation.NoopDataGeneratorMonitor;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.generator.inputs.ProfileReader;
import com.scottlogic.deg.generator.outputs.dataset_writers.CsvDataSetWriter;
import com.scottlogic.deg.generator.outputs.dataset_writers.DataSetWriter;
import com.scottlogic.deg.generator.outputs.dataset_writers.MultiDataSetWriter;
import com.scottlogic.deg.generator.outputs.dataset_writers.SourceTracingDataSetWriter;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;
import com.scottlogic.deg.generator.walker.reductive.field_selection_strategy.FixFieldStrategy;
import com.scottlogic.deg.generator.walker.reductive.field_selection_strategy.HierarchicalDependencyFixFieldStrategy;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@CommandLine.Command(
    name = "generate",
    description = "Generates data using a profile file.",
    mixinStandardHelpOptions = true,
    version = "1.0")
public class Generate implements Runnable, GenerationConfigSource {
    @CommandLine.Parameters(index = "0", description = "The path of the profile json file.")
    @SuppressWarnings("unused")
    private File profileFile;

    @CommandLine.Parameters(index = "1", description = "The path to write the generated data file to.")
    @SuppressWarnings("unused")
    private Path outputPath;

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

    @CommandLine.Option(names = {"-w", "--w", "--walker-type"},
        description = "Determines the tree walker that should be used.",
        defaultValue = GenerationConfig.Constants.WalkerTypes.DEFAULT,
        hidden = true)
    @SuppressWarnings("unused")
    private GenerationConfig.TreeWalkerType walkerType;

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
    public void run() {
        GenerationConfig config = new GenerationConfig(this);

        try {
            final Profile profile = new ProfileReader(config.getProfileValidator()).read(profileFile.toPath());

            FixFieldStrategy fixFieldStrategy = new HierarchicalDependencyFixFieldStrategy(profile, new FieldDependencyAnalyser());
            DataGeneratorMonitor monitor = new NoopDataGeneratorMonitor();
            new GenerationEngine(
                new FileOutputTarget(outputPath, getWriter()),
                new DecisionTreeDataGenerator(
                    config,
                    dontPartitionTrees
                        ? new NoopTreePartitioner()
                        : new RelatedFieldTreePartitioner(),
                    dontOptimise
                        ? new NoopDecisionTreeOptimiser()
                        : new MostProlificConstraintOptimiser(),
                    monitor,
                    new ProfileDecisionTreeFactory(),
                    fixFieldStrategy))
                .generateDataSet(profile, config);
        } catch (IOException | InvalidProfileException e) {
            e.printStackTrace();
        }
    }

    private DataSetWriter getWriter(){
        DataSetWriter outputWriter = new CsvDataSetWriter();
        if (this.enableTracing){
            return new MultiDataSetWriter(outputWriter, new SourceTracingDataSetWriter());
        }

        return outputWriter;
    }

    @Override
    public GenerationConfig.DataGenerationType getGenerationType() {
        return generationType;
    }

    @Override
    public GenerationConfig.CombinationStrategyType getCombinationStrategyType() {
        return combinationType;
    }

    @Override
    public GenerationConfig.TreeWalkerType getWalkerType() {
        return walkerType;
    }

    @Override
    public long getMaxRows() {
        return maxRows;
    }

    @Override
    public boolean getValidateProfile() {
        return validateProfile;
    }

    @Override
    public GenerationConfig.ReductionTarget getReductionTarget() {
        return GenerationConfig.ReductionTarget.VALID_RULE;
    }
}
