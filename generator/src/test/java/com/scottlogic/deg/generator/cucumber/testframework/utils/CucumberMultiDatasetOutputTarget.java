package com.scottlogic.deg.generator.cucumber.testframework.utils;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.outputs.targets.MultiDatasetOutputTarget;
import com.scottlogic.deg.generator.outputs.targets.SingleDatasetOutputTarget;

class CucumberMultiDatasetOutputTarget implements MultiDatasetOutputTarget {
    private final CucumberTestState testState;

    @Inject
    CucumberMultiDatasetOutputTarget(CucumberTestState testState) {
        this.testState = testState;
    }

    @Override
    public SingleDatasetOutputTarget getSubTarget(String name) {
        return new InMemoryOutputTarget(testState);
    }
}
