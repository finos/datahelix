package com.scottlogic.deg.generator.outputs.targets;

import java.io.IOException;

/** An interface that OutputTargets implement, to reflect the fact that they're all validated in the same way */
public interface ValidatableOutput {
    default void validate() throws OutputTargetValidationException, IOException {}
}
