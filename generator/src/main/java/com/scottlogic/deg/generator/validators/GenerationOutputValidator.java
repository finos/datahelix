package com.scottlogic.deg.generator.validators;

import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.generator.inputs.validation.ProfileValidator;
import com.scottlogic.deg.generator.outputs.targets.ValidatableOutput;

import java.io.IOException;

public class GenerationOutputValidator implements ProfileValidator {

    @Override
    public void validate(Profile profile) {
        ValidatableOutput validatable = null; // TODO PAUL

        try {
            validatable.validate(profile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
