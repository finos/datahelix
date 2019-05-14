package com.scottlogic.deg.generator.inputs.validation;

import com.scottlogic.deg.common.profile.Profile;

import java.util.Collection;

public interface ProfileValidator {
    Collection<ValidationAlert> validate(Profile profile);
}
