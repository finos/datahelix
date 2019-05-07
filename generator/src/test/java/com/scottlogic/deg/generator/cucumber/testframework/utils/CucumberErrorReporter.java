package com.scottlogic.deg.generator.cucumber.testframework.utils;

import com.scottlogic.deg.generator.validators.ErrorReporter;
import com.scottlogic.deg.schemas.common.ValidationResult;

public class CucumberErrorReporter extends ErrorReporter {
    private final CucumberTestState state;

    public CucumberErrorReporter(CucumberTestState state) {
        this.state = state;
    }

    @Override
    public void display(ValidationResult validationResult) {
        super.display(validationResult);
    }

    @Override
    public void displayException(Exception e) {
        state.addException(e);
    }
}
