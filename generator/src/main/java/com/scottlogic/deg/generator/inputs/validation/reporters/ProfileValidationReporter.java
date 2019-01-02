package com.scottlogic.deg.generator.inputs.validation.reporters;

import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;

import java.util.List;

public interface ProfileValidationReporter {

    void output(List<ValidationAlert> alerts);
}
