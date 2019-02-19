package com.scottlogic.deg.generator;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.generator.inputs.ProfileReader;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;
import com.scottlogic.deg.generator.validators.ErrorReporter;
import com.scottlogic.deg.generator.validators.GenerationConfigValidator;
import com.scottlogic.deg.generator.validators.ValidationResult;

import java.io.IOException;

public class GenerateExecute implements Runnable {
    private final ErrorReporter errorReporter;
    private final GenerationConfig config;
    private final GenerationConfigSource configSource;
    private final GenerationConfigValidator validator;
    private final GenerationEngine generationEngine;
    private final OutputTarget fileOutputTarget;
    private final ProfileReader profileReader;

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

        ValidationResult validationResult = validator.validateCommandLinePreProfile(config);

        if (!validationResult.isValid()) {
            errorReporter.display(validationResult);
            return;
        }

        try {
            Profile profile = profileReader.read(configSource.getProfileFile().toPath());

            validationResult = validator.validateCommandLinePostProfile(profile);
            if (!validationResult.isValid()) {
                errorReporter.display(validationResult);
                return;
            }

            generationEngine.generateDataSet(profile, config, fileOutputTarget);

        } catch (IOException | InvalidProfileException e) {
            e.printStackTrace();
        }
    }
}
