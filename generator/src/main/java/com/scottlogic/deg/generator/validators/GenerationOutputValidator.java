package com.scottlogic.deg.generator.validators;

import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.generator.inputs.validation.ProfileValidator;
import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;
import com.scottlogic.deg.generator.outputs.targets.ValidatableOutput;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GenerationOutputValidator implements ProfileValidator {

    @Override
    public Collection<ValidationAlert> validate(Profile profile) {
        ValidatableOutput validatable = null;
        List<ValidationAlert> errorMessages = new ArrayList<>();

        try {
            validatable.validate(profile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return errorMessages;
    }
}
