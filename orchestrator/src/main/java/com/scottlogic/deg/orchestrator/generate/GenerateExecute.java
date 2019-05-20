package com.scottlogic.deg.orchestrator.generate;

import com.google.inject.Inject;
import com.scottlogic.deg.common.ValidationException;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.generator.StandardGenerationEngine;
import com.scottlogic.deg.generator.generation.AllConfigSource;
import com.scottlogic.deg.generator.inputs.validation.Criticality;
import com.scottlogic.deg.generator.inputs.validation.ProfileValidator;
import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;
import com.scottlogic.deg.generator.inputs.validation.reporters.ProfileValidationReporter;
import com.scottlogic.deg.generator.outputs.targets.SingleDatasetOutputTarget;
import com.scottlogic.deg.profile.reader.ProfileReader;
import com.scottlogic.deg.orchestrator.validator.ConfigValidator;
import com.scottlogic.deg.generator.validators.ErrorReporter;
import com.scottlogic.deg.profile.v0_1.ProfileSchemaValidator;

import java.io.IOException;
import java.util.Collection;

public class GenerateExecute implements Runnable {
    private final ErrorReporter errorReporter;
    private final AllConfigSource configSource;
    private final SingleDatasetOutputTarget singleDatasetOutputTarget;
    private final ConfigValidator configValidator;
    private final StandardGenerationEngine standardGenerationEngine;
    private final ProfileReader profileReader;
    private final ProfileValidator profileValidator;
    private final ProfileSchemaValidator profileSchemaValidator;
    private final ProfileValidationReporter validationReporter;

    @Inject
    GenerateExecute(
        ProfileReader profileReader,
        StandardGenerationEngine standardGenerationEngine,
        AllConfigSource configSource,
        SingleDatasetOutputTarget singleDatasetOutputTarget,
        ConfigValidator configValidator,
        ErrorReporter errorReporter,
        ProfileValidator profileValidator,
        ProfileSchemaValidator profileSchemaValidator,
        ProfileValidationReporter validationReporter) {

        this.profileReader = profileReader;
        this.standardGenerationEngine = standardGenerationEngine;
        this.configSource = configSource;
        this.singleDatasetOutputTarget = singleDatasetOutputTarget;
        this.configValidator = configValidator;
        this.profileSchemaValidator = profileSchemaValidator;
        this.errorReporter = errorReporter;
        this.profileValidator = profileValidator;
        this.validationReporter = validationReporter;
    }

    @Override
    public void run() {

        try {
            configValidator.preProfileChecks(configSource);
            profileSchemaValidator.validateProfile(configSource.getProfileFile());

            Profile profile = profileReader.read(configSource.getProfileFile().toPath());

            profileValidator.validate(profile);

            standardGenerationEngine.generateDataSet(profile, singleDatasetOutputTarget);

        }
        catch (ValidationException e){
            errorReporter.displayValidation(e);
        }
        catch (IOException e) {
            errorReporter.displayException(e);
        }
    }
}
