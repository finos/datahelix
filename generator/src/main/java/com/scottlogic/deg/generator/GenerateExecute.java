package com.scottlogic.deg.generator;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.generator.inputs.ProfileReader;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;
import com.scottlogic.deg.generator.validators.ConfigValidator;
import com.scottlogic.deg.generator.validators.ErrorReporter;
import com.scottlogic.deg.schemas.common.ValidationResult;
import com.scottlogic.deg.schemas.v0_1.ProfileSchemaValidator;

import java.io.IOException;

public class GenerateExecute implements Runnable {
    private final ErrorReporter errorReporter;
    private final GenerationConfig config;
    private final GenerationConfigSource configSource;
    private final ConfigValidator validator;
    private final GenerationEngine generationEngine;
    private final OutputTarget outputTarget;
    private final ProfileReader profileReader;
    private final ProfileSchemaValidator profileSchemaValidator;

    @Inject
    public GenerateExecute(GenerationConfig config,
                           ProfileReader profileReader,
                           GenerationEngine generationEngine,
                           GenerationConfigSource configSource,
                           OutputTarget outputTarget,
                           ConfigValidator validator,
                           ErrorReporter errorReporter,
                           ProfileSchemaValidator profileSchemaValidator) {
        this.config = config;
        this.profileReader = profileReader;
        this.generationEngine = generationEngine;
        this.configSource = configSource;
        this.outputTarget = outputTarget;
        this.profileSchemaValidator = profileSchemaValidator;
        this.validator = validator;
        this.errorReporter = errorReporter;
    }

    @Override
    public void run() {

        ValidationResult validationResult = validator.preProfileChecks(config, configSource);
        ValidationResult profileSchemaValidationResult = profileSchemaValidator.validateProfile(configSource.getProfileFile());
        if (!validationResult.isValid() || !profileSchemaValidationResult.isValid()) {
            errorReporter.display(validationResult);
            errorReporter.display(profileSchemaValidationResult);
            return;
        }

        try {
            Profile profile = profileReader.read(configSource.getProfileFile().toPath());

            validationResult = validator.postProfileChecks(profile, configSource, outputTarget);
            if (!validationResult.isValid()) {
                errorReporter.display(validationResult);
                return;
            }

            generationEngine.generateDataSet(profile, config, outputTarget);

        } catch (IOException | InvalidProfileException e) {
            e.printStackTrace();
        }
    }

}
