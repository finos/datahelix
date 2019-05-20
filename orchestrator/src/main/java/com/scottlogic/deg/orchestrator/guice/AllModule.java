package com.scottlogic.deg.orchestrator.guice;

import com.google.inject.AbstractModule;
import com.scottlogic.deg.generator.generation.AllConfigSource;
import com.scottlogic.deg.generator.guice.GeneratorModule;
import com.scottlogic.deg.orchestrator.validator.ConfigValidator;
import com.scottlogic.deg.orchestrator.validator.GenerationConfigValidator;
import com.scottlogic.deg.profile.guice.ProfileModule;

public class AllModule extends AbstractModule {
    private AllConfigSource configSource;

    public AllModule(AllConfigSource configSource) {
        this.configSource = configSource;
    }

    @Override
    protected void configure() {
        bind(AllConfigSource.class).toInstance(configSource);

        bind(ConfigValidator.class).to(GenerationConfigValidator.class);

        install(new ProfileModule(configSource));
        install(new GeneratorModule(configSource));
    }
}
