package com.scottlogic.deg.generator.cucumber.utils;

import com.google.inject.Inject;

/**
 * Defines an output target which stores the output data into the test state.
 */
public class InMemoryViolatingOutputTarget extends InMemoryOutputTarget {

    @Inject
    public InMemoryViolatingOutputTarget(CucumberTestState testState) {
        super(testState);
    }

    @Override
    public boolean exists() {
        return true;
    }

    @Override
    public boolean isDirectory() {
        return true;
    }

}