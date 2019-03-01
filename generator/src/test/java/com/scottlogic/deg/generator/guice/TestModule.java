package com.scottlogic.deg.generator.guice;

import com.google.inject.AbstractModule;
import com.scottlogic.deg.generator.ConfigSource;
import com.scottlogic.deg.generator.generation.TestGenerationConfigSource;

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
        bind(ConfigSource.class).toInstance(configSource);
    }
}
