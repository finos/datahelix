package com.scottlogic.deg.generator.inputs.validation.reporters;

import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;

import java.util.Collection;

public class NoopProfileValidationReporter implements ProfileValidationReporter {

    @Override
    public GeneratorContinuation output(Collection<ValidationAlert> alerts) {
        return GeneratorContinuation.CONTINUE;
    }
}
