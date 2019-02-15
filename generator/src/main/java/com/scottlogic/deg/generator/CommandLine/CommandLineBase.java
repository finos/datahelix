package com.scottlogic.deg.generator.CommandLine;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.scottlogic.deg.generator.ConfigSource;
import com.scottlogic.deg.generator.Guice.BaseModule;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;

/**
 * Abstract class to provide a generic run implementation for command line classes.
 */
public abstract class CommandLineBase implements Runnable, ConfigSource {
    @Override
    public void run() {
        BaseModule container = new BaseModule(this);
        Injector injector = Guice.createInjector(container);

        Runnable task = injector.getInstance(getExecutorType());

        task.run();
    }

    protected abstract Class<? extends Runnable> getExecutorType();
}