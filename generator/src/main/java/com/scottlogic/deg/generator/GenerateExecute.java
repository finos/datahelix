package com.scottlogic.deg.generator;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.CommandLine.GenerateCommandLine;
import com.scottlogic.deg.generator.generation.*;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.generator.inputs.ProfileReader;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;

import java.io.IOException;

public class GenerateExecute implements Runnable {
    private final GenerationConfig config;
    private final ProfileReader profileReader;
    private final GenerationEngine generationEngine;
    private final GenerateCommandLine commandLine;
    private final FileOutputTarget fileOutputTarget;

    @Inject
    public GenerateExecute(GenerationConfig config,
                           ProfileReader profileReader,
                           GenerationEngine generationEngine,
                           GenerateCommandLine commandLine,
                           FileOutputTarget fileOutputTarget) {
        this.config = config;
        this.profileReader = profileReader;
        this.generationEngine = generationEngine;
        this.commandLine = commandLine;
        this.fileOutputTarget = fileOutputTarget;
    }

    @Override
    public void run() {
        try {
            Profile profile = this.profileReader.read(this.commandLine.getProfileFile().toPath());

            generationEngine.generateDataSet(profile, config, fileOutputTarget);

        } catch (IOException | InvalidProfileException e) {
            e.printStackTrace();
        }
    }
}
