package com.scottlogic.deg.generator.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.inputs.validation.NoopProfileValidator;
import com.scottlogic.deg.generator.inputs.validation.ProfileValidator;
import com.scottlogic.deg.generator.inputs.validation.ProfileContradictionsValidator;

public class ProfileValidatorProvider implements Provider<ProfileValidator> {
    private final GenerationConfigSource configSource;
    private final ProfileContradictionsValidator contradictionCheckingValidator;

    @Inject
    public ProfileValidatorProvider(GenerationConfigSource configSource, ProfileContradictionsValidator contradictionCheckingValidator) {
        this.configSource = configSource;
        this.contradictionCheckingValidator = contradictionCheckingValidator;
    }

    @Override
    public ProfileValidator get() {
        if(configSource.getValidateProfile()) {
            return contradictionCheckingValidator;
        }
        return new NoopProfileValidator();
    }
}
