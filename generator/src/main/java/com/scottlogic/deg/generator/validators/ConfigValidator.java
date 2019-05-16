package com.scottlogic.deg.generator.validators;

import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;

import java.util.Collection;

public interface ConfigValidator {
    Collection<ValidationAlert> preProfileChecks(GenerationConfigSource configSource);
}
