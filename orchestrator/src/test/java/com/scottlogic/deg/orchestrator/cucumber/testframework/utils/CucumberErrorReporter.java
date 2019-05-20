package com.scottlogic.deg.orchestrator.cucumber.testframework.utils;

import com.scottlogic.deg.generator.validators.ErrorReporter;
public class CucumberErrorReporter extends ErrorReporter {
    private final CucumberTestState state;

    public CucumberErrorReporter(CucumberTestState state) {
        this.state = state;
    }

    @Override
    public void displayException(Exception e) {
        state.addException(e);
    }

}
