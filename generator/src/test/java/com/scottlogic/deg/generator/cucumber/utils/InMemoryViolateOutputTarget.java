package com.scottlogic.deg.generator.cucumber.utils;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Defines an output target which stores the output data into the test state.
 */
public class InMemoryViolateOutputTarget extends InMemoryOutputTarget {

    @Inject
    public InMemoryViolateOutputTarget(CucumberTestState testState) {
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
