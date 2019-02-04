package com.scottlogic.deg.generator;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.generation.*;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.generator.inputs.ProfileReader;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;

import java.io.IOException;

public class GenerateExecute implements Runnable {
    private final GenerationConfig config;
    private final ProfileReader profileReader;
    private final GenerationEngine generationEngine;
    private final GenerationConfigSource configSource;
    private final OutputTarget fileOutputTarget;

    @Inject
    public GenerateExecute(GenerationConfig config,
                           ProfileReader profileReader,
                           GenerationEngine generationEngine,
                           GenerationConfigSource configSource,
                           OutputTarget fileOutputTarget) {
        this.config = config;
        this.profileReader = profileReader;
        this.generationEngine = generationEngine;
        this.configSource = configSource;
        this.fileOutputTarget = fileOutputTarget;
    }

    @Override
    public void run() {

        if (config.getDataGenerationType() == GenerationConfig.DataGenerationType.RANDOM
            && config.getMaxRows() == GenerationConfig.Constants.DEFAULT_MAX_ROWS) {

            System.err.println("RANDOM mode requires max row limit\nuse -n=<row limit> option");
            return;
        }

        try {
            Profile profile = this.profileReader.read(this.configSource.getProfileFile().toPath());

            generationEngine.generateDataSet(profile, config, fileOutputTarget);

        } catch (IOException | InvalidProfileException e) {
            e.printStackTrace();
        }
    }
}
