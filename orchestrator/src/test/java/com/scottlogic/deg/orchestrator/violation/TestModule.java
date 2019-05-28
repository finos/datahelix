package com.scottlogic.deg.orchestrator.violation;

import com.google.inject.AbstractModule;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;

/**
 * Class which defines bindings for Guice injection specific for integration testing.
 */
public class TestModule extends AbstractModule {

    private final TestGenerationConfigSource configSource;

    public TestModule(TestGenerationConfigSource configSource) {
        this.configSource = configSource;
    }

    @Override
    public void configure() {
        bind(GenerationConfigSource.class).toInstance(configSource);
    }
}
