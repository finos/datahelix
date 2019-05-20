package com.scottlogic.deg.generator.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.inputs.validation.*;
import com.scottlogic.deg.generator.validators.GenerationOutputValidator;

import java.util.ArrayList;

public class ProfileValidatorProvider implements Provider<ProfileValidator> {
    private final GenerationConfigSource configSource;
    private final TypingRequiredPerFieldValidator untypedValidator;
    private final GenerationOutputValidator outputValidator;

    @Inject
    public ProfileValidatorProvider(
        GenerationConfigSource configSource,
        TypingRequiredPerFieldValidator untypedValidator,
        GenerationOutputValidator outputValidator) {

        this.configSource = configSource;
        this.untypedValidator = untypedValidator;
        this.outputValidator = outputValidator;
    }

    @Override
    public ProfileValidator get() {
        ArrayList<ProfileValidator> validators = new ArrayList<>();

        if(configSource.requireFieldTyping()) {
            validators.add(untypedValidator);
        }

        validators.add(outputValidator);

        return new MultipleProfileValidator(validators);
    }
}
