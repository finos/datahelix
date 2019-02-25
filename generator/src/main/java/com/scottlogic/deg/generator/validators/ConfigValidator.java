package com.scottlogic.deg.generator.validators;

import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;

public interface ConfigValidator {

    ValidationResult validatePreProfile(GenerationConfig config, GenerationConfigSource configSource);

    ValidationResult validateCommandLinePostProfile(
        Profile profile,
        GenerationConfigSource configSource,
        OutputTarget outputTarget
    );

}
