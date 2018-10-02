package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.combination_strategies.FieldExhaustiveCombinationStrategy;
import com.scottlogic.deg.generator.outputs.FileSystemDataSetOutputter;
import picocli.CommandLine;

import java.io.File;
import java.nio.file.Path;

@CommandLine.Command(description = "Generates data using a profile file.",
    name = "generate", mixinStandardHelpOptions = true, version = "1.0")
public class Generate implements Runnable {

    @CommandLine.Parameters(index = "0", description = "The path of the profile json file.")
    private File sourceFile;

    @CommandLine.Parameters(index = "1", description = "The directory into which generated data should be saved.")
    private Path outputDir;

    @CommandLine.Option(names = {"-t", "--t"},
        description = "Determines the type of data generation performed (FullSequential, Interesting, Random).",
        defaultValue = "Interesting")
    private GenerationConfig.DataGenerationType generationType = GenerationConfig.DataGenerationType.Interesting;

    public static void main(String[] args) throws Exception {
        CommandLine.run(new Generate(), args);
    }

    @Override
    public void run() {

        GenerationConfig config = new GenerationConfig(
            generationType,
            new FieldExhaustiveCombinationStrategy());

        new GenerationEngine(
            new FileSystemDataSetOutputter(outputDir.toString()))
            .generateTestCases(sourceFile.getAbsolutePath(), config);
    }
}
