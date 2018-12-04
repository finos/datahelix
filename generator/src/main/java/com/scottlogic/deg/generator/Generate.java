package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.decisiontree.DecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.NoopDecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.NoopTreePartitioner;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.RelatedFieldTreePartitioner;
import com.scottlogic.deg.generator.generation.DataGenerator;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.SystemOutDataGeneratorMonitor;
import com.scottlogic.deg.generator.generation.combination_strategies.ExhaustiveCombinationStrategy;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.generator.inputs.ProfileReader;
import com.scottlogic.deg.generator.outputs.dataset_writers.CsvDataSetWriter;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;
import com.scottlogic.deg.generator.walker.DecisionTreeWalker;
import com.scottlogic.deg.generator.walker.DecisionTreeWalkerFactory;
import com.scottlogic.deg.generator.walker.RuntimeDecisionTreeWalkerFactory;
import com.scottlogic.deg.generator.walker.reductive.HierarchicalDependencyFixFieldStrategy;
import com.scottlogic.deg.generator.walker.reductive.FixFieldStrategy;
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
    public static final String defaultTreeWalkerType = "cartesian_product";

    @CommandLine.Parameters(index = "0", description = "The path of the profile json file.")
    private File profileFile;

    @CommandLine.Parameters(index = "1", description = "The path to write the generated data file to.")
    private Path outputPath;

    @CommandLine.Option(names = {"-t", "--t"},
        description = "Determines the type of data generation performed (FULL_SEQUENTIAL, INTERESTING, RANDOM).",
        defaultValue = "INTERESTING")
    private GenerationConfig.DataGenerationType generationType;

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
        defaultValue = defaultTreeWalkerType,
        hidden = true)
    private GenerationConfig.TreeWalkerType walkerType;

    @Override
    public void run() {
        GenerationConfig config = new GenerationConfig(
            generationType,
            walkerType,
            new ExhaustiveCombinationStrategy());

        try {
            final Profile profile = new ProfileReader().read(profileFile.toPath());
            FixFieldStrategy fixFieldStrategy = new HierarchicalDependencyFixFieldStrategy(profile);
            DecisionTreeWalkerFactory treeWalkerFactory = new RuntimeDecisionTreeWalkerFactory(config, fixFieldStrategy);
            DecisionTreeWalker treeWalker = treeWalkerFactory.getDecisionTreeWalker();

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
                    new SystemOutDataGeneratorMonitor()))
                .generateDataSet(profile, config);
        } catch (IOException | InvalidProfileException e) {
            e.printStackTrace();
        }
    }
}
