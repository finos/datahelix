package com.scottlogic.deg.generator;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.CommandLine.GenerationConfigValidator;
import com.scottlogic.deg.generator.CommandLine.ValidationResult;
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
    private final GenerationConfigValidator validator;
    private final ErrorReporter errorReporter;

    @Inject
    public GenerateExecute(GenerationConfig config,
                           ProfileReader profileReader,
                           GenerationEngine generationEngine,
                           GenerationConfigSource configSource,
                           OutputTarget fileOutputTarget,
                           GenerationConfigValidator validator,
                           ErrorReporter errorReporter) {
        this.config = config;
        this.profileReader = profileReader;
        this.generationEngine = generationEngine;
        this.configSource = configSource;
        this.fileOutputTarget = fileOutputTarget;
        this.validator = validator;
        this.errorReporter = errorReporter;
    }

    @Override
    public void run() {

        ValidationResult validationResult = validator.validateCommandLine(config);

        if (!validationResult.isValid()) {
            errorReporter.display(validationResult);
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
