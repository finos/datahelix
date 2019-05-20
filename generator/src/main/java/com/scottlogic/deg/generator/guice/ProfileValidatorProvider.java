package com.scottlogic.deg.generator.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.inputs.validation.*;

import java.util.ArrayList;

public class ProfileValidatorProvider implements Provider<ProfileValidator> {
    private final GenerationConfigSource configSource;
    private final TypingRequiredPerFieldValidator untypedValidator;

    @Inject
    public ProfileValidatorProvider(
        GenerationConfigSource configSource,
        TypingRequiredPerFieldValidator untypedValidator) {

        this.configSource = configSource;
        this.untypedValidator = untypedValidator;
    }

    @Override
    public ProfileValidator get() {
        ArrayList<ProfileValidator> validators = new ArrayList<>();

        if(configSource.requireFieldTyping()) {
            validators.add(untypedValidator);
        }
        return new MultipleProfileValidator(validators);
    }
}
