package com.scottlogic.deg.generator.inputs.validation;

import com.scottlogic.deg.generator.Profile;

import java.util.Collection;
import java.util.Collections;

public class NoopProfileValidator implements ProfileValidator {

    @Override
    public Collection<ValidationAlert> validate(Profile profile) {
        return Collections.emptySet();
    }
}
