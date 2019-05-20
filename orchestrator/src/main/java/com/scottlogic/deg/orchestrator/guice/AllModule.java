package com.scottlogic.deg.orchestrator.guice;

import com.google.inject.AbstractModule;
import com.scottlogic.deg.generator.generation.AllConfigSource;
import com.scottlogic.deg.generator.guice.GeneratorModule;
import com.scottlogic.deg.profile.guice.ProfileModule;

public class AllModule extends AbstractModule {
    private AllConfigSource configSource;

    public AllModule(AllConfigSource configSource) {
        this.configSource = configSource;
    }

    @Override
    protected void configure() {
        bind(AllConfigSource.class).toInstance(configSource);

        install(new ProfileModule(configSource));
        install(new GeneratorModule(configSource));
    }
}
