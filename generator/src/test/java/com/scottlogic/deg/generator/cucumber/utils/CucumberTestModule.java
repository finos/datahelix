package com.scottlogic.deg.generator.cucumber.utils;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.scottlogic.deg.generator.GenerationEngine;
import com.scottlogic.deg.generator.StandardGenerationEngine;
import com.scottlogic.deg.generator.ViolationGenerationEngine;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.inputs.ProfileReader;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;

/**
 * Class which defines bindings for Guice injection specific for cucumber testing. The test state is persisted through
 * the various classes by binding the CucumberTestState object to the instance specified here.
 */
public class CucumberTestModule extends AbstractModule {
    private final CucumberTestState testState;

    public CucumberTestModule(CucumberTestState testState) {
        this.testState = testState;
    }

    @Override
    public void configure() {
        bind(CucumberTestState.class).toInstance(testState);
        bind(ProfileReader.class).to(CucumberProfileReader.class);
        bind(GenerationConfigSource.class).to(CucumberGenerationConfigSource.class);
        bind(OutputTarget.class).to(InMemoryOutputTarget.class).in(Singleton.class);

        if (testState.shouldViolate) {
            bind(GenerationEngine.class).to(ViolationGenerationEngine.class);
        } else {
            bind(GenerationEngine.class).to(StandardGenerationEngine.class);
        }
    }
}
