package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.analysis.FieldDependencyAnalyser;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.NoopDecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.NoopTreePartitioner;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.RelatedFieldTreePartitioner;
import com.scottlogic.deg.generator.generation.DataGenerator;
import com.scottlogic.deg.generator.generation.DataGeneratorMonitor;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.NoopDataGeneratorMonitor;
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
public class GenerateTestCases implements Runnable {
    @CommandLine.Parameters(index = "0", description = "The path of the profile json file.")
    private File profileFile;

    @CommandLine.Parameters(index = "1", description = "The directory into which generated data should be saved.")
    private Path outputDir;

    @CommandLine.Option(names = {"-t", "--t"},
        description = "Determines the type of data generation performed (FULL_SEQUENTIAL, INTERESTING, RANDOM).",
        defaultValue = Generate.interesting_generation_type)
    private GenerationConfig.DataGenerationType generationType;

    @CommandLine.Option(names = {"-c", "--c"},
        description = "Determines the type of combination strategy used (PINNING, EXHAUSTIVE, MINIMAL).",
        defaultValue = Generate.pinning_combination_strategy)
    private GenerationConfig.CombinationStrategyType combinationType;

    @CommandLine.Option(names = {"-w", "--w"},
        description = "Determines the tree walker that should be used.",
        defaultValue = Generate.cartestian_product_walker_type,
        hidden = true)
    private GenerationConfig.TreeWalkerType walkerType;

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

    @Override
    public void run() {
        GenerationConfig config = new GenerationConfig(
            generationType,
            walkerType,
            combinationType);

        try {
            DataGeneratorMonitor monitor = new NoopDataGeneratorMonitor();
            final Profile profile = new ProfileReader().read(profileFile.toPath());
            DecisionTreeWalkerFactory walkerFactory = new RuntimeDecisionTreeWalkerFactory(config, monitor, new HierarchicalDependencyFixFieldStrategy(profile, new FieldDependencyAnalyser()));

            new GenerationEngine(
                new DirectoryOutputTarget(outputDir, new CsvDataSetWriter()),
                new DataGenerator(
                    walkerFactory.getDecisionTreeWalker(outputDir),
                    dontPartitionTrees
                        ? new NoopTreePartitioner()
                        : new RelatedFieldTreePartitioner(),
                    dontOptimise
                        ? new NoopDecisionTreeOptimiser()
                        : new DecisionTreeOptimiser(),
                    monitor))
                .generateTestCases(profile, config);
        } catch (IOException | InvalidProfileException e) {
            e.printStackTrace();
        }
    }
}
