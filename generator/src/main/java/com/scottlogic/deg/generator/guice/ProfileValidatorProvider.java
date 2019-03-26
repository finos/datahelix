package com.scottlogic.deg.generator.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.inputs.validation.*;

public class ProfileValidatorProvider implements Provider<ProfileValidator> {
    private final GenerationConfigSource configSource;
    private final ProfileContradictionsValidator contradictionCheckingValidator;
    private final TypingRequiredPerFieldValidator untypedValidator;

    @Inject
    public ProfileValidatorProvider(
        GenerationConfigSource configSource,
        ProfileContradictionsValidator contradictionCheckingValidator,
        TypingRequiredPerFieldValidator untypedValidator) {

        this.configSource = configSource;
        this.contradictionCheckingValidator = contradictionCheckingValidator;
        this.untypedValidator = untypedValidator;
    }

    @Override
    public ProfileValidator get() {
        if(configSource.getValidateProfile()) {
            return new MultipleProfileValidator(
                contradictionCheckingValidator,
                untypedValidator);
        }

        return untypedValidator;
    }
}
