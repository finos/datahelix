package com.scottlogic.deg.generator.Guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.inputs.validation.NoopProfileValidator;
import com.scottlogic.deg.generator.inputs.validation.ProfileValidator;
import com.scottlogic.deg.generator.inputs.validation.ReportingProfileValidator;
import com.scottlogic.deg.generator.inputs.validation.reporters.ProfileValidationReporter;

public class ProfileValidatorProvider implements Provider<ProfileValidator> {
    private final GenerationConfigSource configSource;
    private final ProfileValidationReporter validationReporter;

    @Inject
    public ProfileValidatorProvider(GenerationConfigSource configSource, ProfileValidationReporter validationReporter) {
        this.configSource = configSource;
        this.validationReporter = validationReporter;
    }

    @Override
    public ProfileValidator get() {
        if(configSource.getValidateProfile()) {
            return new ReportingProfileValidator(validationReporter);
        }
        return new NoopProfileValidator();
    }
}
