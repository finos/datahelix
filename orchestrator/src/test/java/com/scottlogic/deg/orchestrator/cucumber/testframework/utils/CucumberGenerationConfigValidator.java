package com.scottlogic.deg.orchestrator.cucumber.testframework.utils;

import com.scottlogic.deg.generator.generation.AllConfigSource;
import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;
import com.scottlogic.deg.orchestrator.validator.ConfigValidator;

import java.util.Collection;
import java.util.Collections;

public class CucumberGenerationConfigValidator implements ConfigValidator {

    @Override
    public Collection<ValidationAlert> preProfileChecks(AllConfigSource configSource) {
        return Collections.emptySet();
    }
}

