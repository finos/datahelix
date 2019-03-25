package com.scottlogic.deg.generator.cucumber.utils;

import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;
import com.scottlogic.deg.generator.validators.ConfigValidator;
import com.scottlogic.deg.schemas.common.ValidationResult;

import java.util.ArrayList;

public class CucumberGenerationConfigValidator implements ConfigValidator {

    @Override
    public ValidationResult preProfileChecks(GenerationConfig config, GenerationConfigSource configSource) {
        return new ValidationResult(new ArrayList<>());
    }

    @Override
    public ValidationResult postProfileChecks(Profile profile, GenerationConfigSource configSource, OutputTarget outputTarget) {
        return new ValidationResult(new ArrayList<>());
    }
}
