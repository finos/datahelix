package com.scottlogic.deg.orchestrator.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.scottlogic.deg.generator.guice.GeneratorModule;
import com.scottlogic.deg.orchestrator.visualise.ReductiveWalkerProvider;
import com.scottlogic.deg.generator.walker.ReductiveTreeWalker;
import com.scottlogic.deg.output.guice.OutputModule;
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
        install(new OutputModule(configSource));

        Module generatorModule = Modules.override(new GeneratorModule(configSource))
            .with(new ReductiveVisualiseOverride());
        install(generatorModule);
    }

    private static class ReductiveVisualiseOverride extends AbstractModule {
        @Override
        protected void configure() {
            bind(ReductiveTreeWalker.class).toProvider(ReductiveWalkerProvider.class);
        }
    }
}
