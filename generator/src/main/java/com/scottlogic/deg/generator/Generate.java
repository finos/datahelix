package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.combination_strategies.FieldExhaustiveCombinationStrategy;
import com.scottlogic.deg.generator.outputs.dataset_writers.CsvDataSetWriter;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;
import picocli.CommandLine;

import java.io.File;
import java.nio.file.Path;

@CommandLine.Command(
    name = "generate",
    description = "Generates data using a profile file.",
    mixinStandardHelpOptions = true,
    version = "1.0")
public class Generate implements Runnable {
    @CommandLine.Parameters(index = "0", description = "The path of the profile json file.")
    private File profileFile;

    @CommandLine.Parameters(index = "1", description = "The path to write the generated data file to.")
    private Path outputPath;

    @CommandLine.Option(names = {"-t", "--t"},
        description = "Determines the type of data generation performed (FullSequential, Interesting, Random).",
        defaultValue = "Interesting")
    private GenerationConfig.DataGenerationType generationType = GenerationConfig.DataGenerationType.Interesting;

    @Override
    public void run() {
        GenerationConfig config = new GenerationConfig(
            generationType,
            new FieldExhaustiveCombinationStrategy());

        new GenerationEngine(
                new FileOutputTarget(outputPath, new CsvDataSetWriter()))
            .generateDataSet(profileFile.toPath(), config);
    }
}
