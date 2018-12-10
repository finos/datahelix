package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.analysis.FieldDependencyAnalyser;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeGenerator;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.NoopDecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.NoopTreePartitioner;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.RelatedFieldTreePartitioner;
import com.scottlogic.deg.generator.generation.*;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.generator.inputs.ProfileReader;
import com.scottlogic.deg.generator.outputs.dataset_writers.CsvDataSetWriter;
import com.scottlogic.deg.generator.outputs.targets.DirectoryOutputTarget;
import com.scottlogic.deg.generator.walker.DecisionTreeWalkerFactory;
import com.scottlogic.deg.generator.walker.RuntimeDecisionTreeWalkerFactory;
import com.scottlogic.deg.generator.walker.reductive.field_selection_strategy.HierarchicalDependencyFixFieldStrategy;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@CommandLine.Command(
    name = "generateTestCases",
    description = "Generates valid and violating data using a profile file.",
    mixinStandardHelpOptions = true,
    version = "1.0")
public class GenerateTestCases implements Runnable, GenerationConfigSource {
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

    @Override
    public void run() {
        GenerationConfig config = new GenerationConfig(this);

        try {
            DataGeneratorMonitor monitor = new NoopDataGeneratorMonitor();
            final Profile profile = new ProfileReader().read(profileFile.toPath());
            DecisionTreeWalkerFactory walkerFactory = new RuntimeDecisionTreeWalkerFactory(config, monitor, new HierarchicalDependencyFixFieldStrategy(profile, new FieldDependencyAnalyser()));

            new GenerationEngine(
                new DirectoryOutputTarget(
                    outputDir,
                    getFilenameWithoutExtension(profileFile.getName()),
                    new CsvDataSetWriter()),
                new DataGenerator(
                    walkerFactory.getDecisionTreeWalker(outputDir),
                    dontPartitionTrees
                        ? new NoopTreePartitioner()
                        : new RelatedFieldTreePartitioner(),
                    dontOptimise
                        ? new NoopDecisionTreeOptimiser()
                        : new DecisionTreeOptimiser(),
                    monitor),
                new DecisionTreeGenerator())
                .generateTestCases(profile, config);
        } catch (IOException | InvalidProfileException e) {
            e.printStackTrace();
        }
    }

    private String getFilenameWithoutExtension(String fileNameWithExtension) {
        return fileNameWithExtension.replaceFirst("[.][^.]+$", "");
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
}
