package com.scottlogic.deg.generator.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.inputs.validation.*;
import com.scottlogic.deg.generator.validators.GenerationConfigValidator;
import com.scottlogic.deg.generator.validators.GenerationOutputValidator;

import java.util.ArrayList;

public class ProfileValidatorProvider implements Provider<ProfileValidator> {
    private final GenerationConfigSource configSource;
    private final ProfileContradictionsValidator contradictionCheckingValidator;
    private final TypingRequiredPerFieldValidator untypedValidator;
    private final GenerationOutputValidator outputValidator;

    @Inject
    public ProfileValidatorProvider(
        GenerationConfigSource configSource,
        ProfileContradictionsValidator contradictionCheckingValidator,
        TypingRequiredPerFieldValidator untypedValidator,
        GenerationOutputValidator outputValidator) {

        this.configSource = configSource;
        this.contradictionCheckingValidator = contradictionCheckingValidator;
        this.untypedValidator = untypedValidator;
        this.outputValidator = outputValidator;
    }

    @Override
    public ProfileValidator get() {
        ArrayList<ProfileValidator> validators = new ArrayList<>();

        if(configSource.getValidateProfile()) {
            validators.add(contradictionCheckingValidator);
        }

        if(configSource.requireFieldTyping()) {
            validators.add(untypedValidator);
        }

        validators.add(outputValidator);

        return new MultipleProfileValidator(validators);
    }
}
