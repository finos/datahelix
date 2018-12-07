package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.analysis.FieldDependencyAnalyser;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.NoopDecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.NoopTreePartitioner;
import com.scottlogic.deg.generator.generation.*;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.RelatedFieldTreePartitioner;
import com.scottlogic.deg.generator.generation.NoopDataGeneratorMonitor;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.generator.inputs.ProfileReader;
import com.scottlogic.deg.generator.outputs.dataset_writers.CsvDataSetWriter;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;
import com.scottlogic.deg.generator.walker.DecisionTreeWalker;
import com.scottlogic.deg.generator.walker.DecisionTreeWalkerFactory;
import com.scottlogic.deg.generator.walker.RuntimeDecisionTreeWalkerFactory;
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
public class Generate implements Runnable {
    public static final String cartestian_product_walker_type = "CARTESIAN_PRODUCT";
    public static final String pinning_combination_strategy = "PINNING";
    public static final String interesting_generation_type = "INTERESTING";

    @CommandLine.Parameters(index = "0", description = "The path of the profile json file.")
    private File profileFile;

    @CommandLine.Parameters(index = "1", description = "The path to write the generated data file to.")
    private Path outputPath;

    @CommandLine.Option(names = {"-t", "--t"},
        description = "Determines the type of data generation performed (FULL_SEQUENTIAL, INTERESTING, RANDOM).",
        defaultValue = interesting_generation_type)
    private GenerationConfig.DataGenerationType generationType;

    @CommandLine.Option(names = {"-c", "--c"},
        description = "Determines the type of combination strategy used (PINNING, EXHAUSTIVE, MINIMAL).",
        defaultValue = pinning_combination_strategy)
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

    @CommandLine.Option(names = {"-w", "--w"},
        description = "Determines the tree walker that should be used.",
        defaultValue = cartestian_product_walker_type,
        hidden = true)
    private GenerationConfig.TreeWalkerType walkerType;

    @Override
    public void run() {
        GenerationConfig config = new GenerationConfig(
            generationType,
            walkerType,
            combinationType);

        try {
            final Profile profile = new ProfileReader().read(profileFile.toPath());

            FixFieldStrategy fixFieldStrategy = new HierarchicalDependencyFixFieldStrategy(profile, new FieldDependencyAnalyser());
            DataGeneratorMonitor monitor = new NoopDataGeneratorMonitor();
            DecisionTreeWalkerFactory treeWalkerFactory = new RuntimeDecisionTreeWalkerFactory(config, monitor, fixFieldStrategy);
            DecisionTreeWalker treeWalker = treeWalkerFactory.getDecisionTreeWalker(outputPath.getParent());

            new GenerationEngine(
                new FileOutputTarget(outputPath, new CsvDataSetWriter()),
                new DataGenerator(
                    treeWalker,
                    dontPartitionTrees
                        ? new NoopTreePartitioner()
                        : new RelatedFieldTreePartitioner(),
                    dontOptimise
                        ? new NoopDecisionTreeOptimiser()
                        : new DecisionTreeOptimiser(),
                    monitor))
                .generateDataSet(profile, config);
        } catch (IOException | InvalidProfileException e) {
            e.printStackTrace();
        }
    }
}
