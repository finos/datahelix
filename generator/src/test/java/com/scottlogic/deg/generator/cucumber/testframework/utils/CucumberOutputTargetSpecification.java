package com.scottlogic.deg.generator.cucumber.testframework.utils;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.commandline.OutputTargetSpecification;
import com.scottlogic.deg.generator.outputs.targets.MultiDatasetOutputTarget;
import com.scottlogic.deg.generator.outputs.targets.SingleDatasetOutputTarget;

class CucumberOutputTargetSpecification implements OutputTargetSpecification {
    private final CucumberTestState testState;

    @Inject
    CucumberOutputTargetSpecification(CucumberTestState testState) {
        this.testState = testState;
    }

    @Override
    public SingleDatasetOutputTarget asFilePath() {
        return new InMemoryOutputTarget(testState);
    }

    @Override
    public MultiDatasetOutputTarget asViolationDirectory() {
        return new CucumberMultiDatasetOutputTarget(testState);
    }
}
