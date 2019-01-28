package com.scottlogic.deg.generator.CommandLine;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.scottlogic.deg.generator.Guice.BaseModule;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;

public abstract class CommandLineBase implements Runnable, GenerationConfigSource {
    @Override
    public void run() {
        BaseModule container = new BaseModule(this);
        Injector injector = Guice.createInjector(container);

        Runnable task = injector.getInstance(getExecutorType());

        task.run();
    }

    protected abstract Class<? extends Runnable> getExecutorType();
}
