package com.scottlogic.deg.generator.validators;

import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;

import java.io.IOException;

public interface ConfigValidator {

    ValidationResult preProfileChecks(GenerationConfig config, GenerationConfigSource configSource);

    ValidationResult postProfileChecks(
        Profile profile,
        GenerationConfigSource configSource,
        OutputTarget outputTarget
    ) throws IOException;

}
