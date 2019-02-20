package com.scottlogic.deg.generator.validators;

import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.generation.GenerationConfig;

public interface ConfigValidator {

    ValidationResult validatePreProfile(GenerationConfig config);

    ValidationResult validateCommandLinePostProfile(Profile profile);

}
