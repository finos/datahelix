package com.scottlogic.deg.generator.inputs.validation.reporters;

import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;

import java.util.Collection;

public interface ProfileValidationReporter {
    GeneratorContinuation output(Collection<ValidationAlert> alerts);
}
