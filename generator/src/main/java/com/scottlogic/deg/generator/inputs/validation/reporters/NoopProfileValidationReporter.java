package com.scottlogic.deg.generator.inputs.validation.reporters;

import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;

import java.util.List;

public class NoopProfileValidationReporter implements ProfileValidationReporter {

    @Override
    public void output(List<ValidationAlert> alerts) {

    }
}
