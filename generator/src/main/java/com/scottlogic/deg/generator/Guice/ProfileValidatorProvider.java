package com.scottlogic.deg.generator.Guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.CommandLine.GenerateCommandLine;
import com.scottlogic.deg.generator.inputs.validation.NoopProfileValidator;
import com.scottlogic.deg.generator.inputs.validation.ProfileValidator;
import com.scottlogic.deg.generator.inputs.validation.ReportingProfileValidator;
import com.scottlogic.deg.generator.inputs.validation.reporters.ProfileValidationReporter;

public class ProfileValidatorProvider implements Provider<ProfileValidator> {

    private final GenerateCommandLine commandLine;
    private final ProfileValidationReporter validationReporter;

    @Inject
    public ProfileValidatorProvider(GenerateCommandLine commandLine, ProfileValidationReporter validationReporter) {
        this.commandLine = commandLine;
        this.validationReporter = validationReporter;
    }

    @Override
    public ProfileValidator get() {
        if(commandLine.shouldValidateProfile()) {
            return new ReportingProfileValidator(validationReporter);
        }
        return new NoopProfileValidator();
    }
}
