package com.scottlogic.deg.generator;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.CommandLine.GenerateTestCasesCommandLine;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.generator.inputs.JsonProfileReader;
import com.scottlogic.deg.generator.outputs.targets.DirectoryOutputTarget;
import picocli.CommandLine;

import java.io.IOException;

@CommandLine.Command(
    name = "generateTestCases",
    description = "Generates valid and violating data using a profile file.",
    mixinStandardHelpOptions = true,
    version = "1.0")
public class GenerateTestCasesExecute implements Runnable {
    private final GenerationConfig config;
    private final JsonProfileReader profileReader;
    private final GenerateTestCasesCommandLine commandLine;
    private final GenerationEngine generationEngine;
    private final DirectoryOutputTarget directoryOutputTarget;

    @Inject
    public GenerateTestCasesExecute(
        GenerationConfig config,
        JsonProfileReader profileReader,
        GenerateTestCasesCommandLine commandLine,
        GenerationEngine generationEngine,
        DirectoryOutputTarget directoryOutputTarget){

        this.config = config;
        this.profileReader = profileReader;
        this.commandLine = commandLine;
        this.generationEngine = generationEngine;
        this.directoryOutputTarget = directoryOutputTarget;
    }

    @Override
    public void run() {
        try {
            final Profile profile = profileReader.read(this.commandLine.getProfileFile().toPath());

            this.generationEngine.generateTestCases(profile, config, directoryOutputTarget);
        } catch (IOException | InvalidProfileException e) {
            e.printStackTrace();
        }
    }
}
