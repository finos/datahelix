package com.scottlogic.deg.generator.validators;

import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;
import com.scottlogic.deg.schemas.common.ValidationResult;

import java.io.IOException;

public interface ConfigValidator {
    ValidationResult preProfileChecks(GenerationConfig config, GenerationConfigSource configSource);
}
