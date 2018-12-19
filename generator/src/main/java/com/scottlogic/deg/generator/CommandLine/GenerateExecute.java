package com.scottlogic.deg.generator.CommandLine;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.GenerationEngine;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.generation.*;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.generator.inputs.ProfileReader;

import java.io.IOException;

public class GenerateExecute implements Runnable {
    private final GenerationConfig config;
    private final ProfileReader profileReader;
    private final GenerationEngine generationEngine;
    private final GenerateCommandLine commandLine;

    @Inject
    public GenerateExecute(GenerationConfig config, ProfileReader profileReader,
                           GenerationEngine generationEngine, GenerateCommandLine commandLine) {
        this.config = config;
        this.profileReader = profileReader;
        this.generationEngine = generationEngine;
        this.commandLine = commandLine;
    }

    @Override
    public void run() {
        try {
            Profile profile = this.profileReader.read(this.commandLine.getProfileFile().toPath());

            generationEngine.generateDataSet(profile, config);
        } catch (IOException | InvalidProfileException e) {
            e.printStackTrace();
        }
    }
}
