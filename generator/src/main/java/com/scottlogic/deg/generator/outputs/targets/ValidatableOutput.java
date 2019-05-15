package com.scottlogic.deg.generator.outputs.targets;

import com.scottlogic.deg.common.profile.Profile;

import java.io.IOException;

/** An interface that OutputTargets implement, to reflect the fact that they're all validated in the same way */
public interface ValidatableOutput {
    default void validate(Profile profile) throws OutputTargetValidationException, IOException {}
}
