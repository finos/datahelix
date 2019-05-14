package com.scottlogic.deg.generator;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.inputs.validation.Criticality;
import com.scottlogic.deg.generator.inputs.validation.ProfileValidator;
import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;
import com.scottlogic.deg.generator.inputs.validation.reporters.ProfileValidationReporter;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.generator.inputs.ProfileReader;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;
import com.scottlogic.deg.generator.validators.ConfigValidator;
import com.scottlogic.deg.generator.validators.ErrorReporter;
import com.scottlogic.deg.profile.common.ValidationResult;
import com.scottlogic.deg.profile.v0_1.ProfileSchemaValidator;

import java.io.IOException;
import java.util.Collection;

public class GenerateExecute implements Runnable {
    private final ErrorReporter errorReporter;
    private final GenerationConfig config;
    private final GenerationConfigSource configSource;
    private final ConfigValidator configValidator;
    private final GenerationEngine generationEngine;
    private final OutputTarget outputTarget;
    private final ProfileReader profileReader;
    private final ProfileValidator profileValidator;
    private final ProfileSchemaValidator profileSchemaValidator;
    private final ProfileValidationReporter validationReporter;

    @Inject
    public GenerateExecute(GenerationConfig config,
                           ProfileReader profileReader,
                           GenerationEngine generationEngine,
                           GenerationConfigSource configSource,
                           OutputTarget outputTarget,
                           ConfigValidator configValidator,
                           ErrorReporter errorReporter,
                           ProfileValidator profileValidator,
                           ProfileSchemaValidator profileSchemaValidator,
                           ProfileValidationReporter validationReporter) {
        this.config = config;
        this.profileReader = profileReader;
        this.generationEngine = generationEngine;
        this.configSource = configSource;
        this.outputTarget = outputTarget;
        this.configValidator = configValidator;
        this.profileSchemaValidator = profileSchemaValidator;
        this.errorReporter = errorReporter;
        this.profileValidator = profileValidator;
        this.validationReporter = validationReporter;
    }

    @Override
    public void run() {
        Collection<ValidationAlert> validationResult = configValidator.preProfileChecks(config, configSource);
        if (!validationResult.isEmpty()) {
            validationReporter.output(validationResult);
            return;
        }

        ValidationResult profileSchemaValidationResult = profileSchemaValidator.validateProfile(configSource.getProfileFile());
        if (!profileSchemaValidationResult.isValid()) {
            errorReporter.display(profileSchemaValidationResult);
            return;
        }

        try {
            Profile profile = profileReader.read(configSource.getProfileFile().toPath());

            Collection<ValidationAlert> alerts = profileValidator.validate(profile);
            validationReporter.output(alerts);
            if (validationResultShouldHaltExecution(alerts)) {
                return;
            }

            generationEngine.generateDataSet(profile, config, outputTarget);

        } catch (IOException | InvalidProfileException e) {
            errorReporter.displayException(e);
        }
    }

    private static boolean validationResultShouldHaltExecution(Collection<ValidationAlert> alerts) {
        return alerts.stream()
            .anyMatch(alert ->
                alert.getCriticality().equals(Criticality.ERROR));
    }

}
