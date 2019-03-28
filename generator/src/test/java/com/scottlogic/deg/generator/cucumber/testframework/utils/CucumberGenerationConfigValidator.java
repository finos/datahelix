package com.scottlogic.deg.generator.cucumber.testframework.utils;

import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;
import com.scottlogic.deg.generator.validators.ConfigValidator;
import com.scottlogic.deg.schemas.common.ValidationResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class CucumberGenerationConfigValidator implements ConfigValidator {

    @Override
    public Collection<ValidationAlert> preProfileChecks(GenerationConfig config, GenerationConfigSource configSource) {
        return Collections.emptySet();
    }
}

