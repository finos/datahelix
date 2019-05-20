package com.scottlogic.deg.orchestrator.violate;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.generator.StandardGenerationEngine;
import com.scottlogic.deg.generator.commandline.OutputTargetSpecification;
import com.scottlogic.deg.generator.generation.AllConfigSource;
import com.scottlogic.deg.generator.inputs.profileviolation.ProfileViolator;
import com.scottlogic.deg.generator.inputs.validation.Criticality;
import com.scottlogic.deg.generator.inputs.validation.ProfileValidator;
import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;
import com.scottlogic.deg.generator.inputs.validation.reporters.ProfileValidationReporter;
import com.scottlogic.deg.generator.utils.FileUtils;
import com.scottlogic.deg.generator.validators.ErrorReporter;
import com.scottlogic.deg.orchestrator.validator.ConfigValidator;
import com.scottlogic.deg.profile.reader.InvalidProfileException;
import com.scottlogic.deg.profile.reader.ProfileReader;
import com.scottlogic.deg.profile.serialisation.ValidationResult;
import com.scottlogic.deg.profile.v0_1.ProfileSchemaValidator;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;

public class ViolateExecute implements Runnable {
    private final ErrorReporter errorReporter;
    private final AllConfigSource configSource;
    private final ConfigValidator configValidator;

    private final OutputTargetSpecification outputTargetSpecification;

    private final ProfileReader profileReader;
    private final ProfileValidator profileValidator;
    private final ProfileSchemaValidator profileSchemaValidator;
    private final ProfileValidationReporter validationReporter;
    private final ProfileViolator profileViolator;
    private final StandardGenerationEngine generationEngine;

    @Inject
    ViolateExecute(
        ProfileReader profileReader,
        AllConfigSource configSource,
        OutputTargetSpecification outputTargetSpecification,
        ConfigValidator configValidator,
        ErrorReporter errorReporter,
        ProfileValidator profileValidator,
        ProfileSchemaValidator profileSchemaValidator,
        ProfileValidationReporter validationReporter,
        ProfileViolator profileViolator,
        StandardGenerationEngine generationEngine) {

        this.profileReader = profileReader;
        this.configSource = configSource;
        this.outputTargetSpecification = outputTargetSpecification;
        this.configValidator = configValidator;
        this.profileSchemaValidator = profileSchemaValidator;
        this.errorReporter = errorReporter;
        this.profileValidator = profileValidator;
        this.validationReporter = validationReporter;
        this.profileViolator = profileViolator;
        this.generationEngine = generationEngine;
    }



    private void doGeneration(Profile profile) throws IOException {
        List<Profile> violatedProfiles = profileViolator.violate(profile);

        if (violatedProfiles.isEmpty()) {
            return;
        }

        DecimalFormat intFormatter = FileUtils.getDecimalFormat(violatedProfiles.size());

        int filename = 1;
        for (Profile violatedProfile : violatedProfiles) {
            generationEngine.generateDataSet(
                violatedProfile,
                outputTargetSpecification.asViolationDirectory().getSubTarget(
                    intFormatter.format(filename))
            );

            filename++;
        }
    }

    @Override
    public void run() {
        Collection<ValidationAlert> validationResult = configValidator.preProfileChecks(configSource);
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

            doGeneration(profile);

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
