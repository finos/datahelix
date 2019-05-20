package com.scottlogic.deg.orchestrator.cucumber.testframework.utils;

import com.scottlogic.deg.common.ValidationException;
import com.scottlogic.deg.generator.validators.ErrorReporter;

import java.util.stream.Collectors;

public class CucumberErrorReporter extends ErrorReporter {
    private final CucumberTestState state;

    public CucumberErrorReporter(CucumberTestState state) {
        this.state = state;
    }

    @Override
    public void displayException(Exception e) {
        state.addException(e);
    }

    @Override
    public void displayValidation(ValidationException e){
        state.testExceptions.addAll(e.errorMessages.stream().map(ValidationException::new).collect(Collectors.toList()));
    }

}
