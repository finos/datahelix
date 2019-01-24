package com.scottlogic.deg.generator.violations;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.CommandLine.GenerateTestCasesCommandLine;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.generator.inputs.ProfileReader;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;
import picocli.CommandLine;

import java.io.IOException;

@CommandLine.Command(
    name = "generateTestCases",
    description = "Generates valid and violating data using a profile file.",
    mixinStandardHelpOptions = true,
    version = "1.0")
public class GenerateTestCasesExecute implements Runnable {
    private final GenerationConfig config;
    private final ProfileReader profileReader;
    private final GenerateTestCasesCommandLine commandLine;
    private final InvalidGenerationEngine generationEngine;
    private FileOutputTarget fileOutputTarget;

    @Inject
    public GenerateTestCasesExecute(
        GenerationConfig config,
        ProfileReader profileReader,
        GenerateTestCasesCommandLine commandLine,
        InvalidGenerationEngine generationEngine,
        FileOutputTarget fileOutputTarget){

        this.config = config;
        this.profileReader = profileReader;
        this.commandLine = commandLine;
        this.generationEngine = generationEngine;
        this.fileOutputTarget = fileOutputTarget;
    }

    @Override
    public void run() {
        try {
            final Profile profile = profileReader.read(this.commandLine.getProfileFile().toPath());

            this.generationEngine.generateDataSet(profile, config, fileOutputTarget);
        } catch (IOException | InvalidProfileException e) {
            e.printStackTrace();
        }
    }
}
