package com.scottlogic.deg.generator.CommandLine;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.scottlogic.deg.generator.Guice.BaseModule;
import com.scottlogic.deg.generator.visualise.VisualiseConfigSource;

/**
 * Abstract class to provide a generic run implementation for command line classes.
 */
public abstract class VisualiseCommandLineBase implements Runnable, VisualiseConfigSource {

    @Override
    public void run() {
        BaseModule container = new BaseModule(this);
        Injector injector = Guice.createInjector(container);

        Runnable task = injector.getInstance(getExecutorType());

        task.run();
    }

    protected abstract Class<? extends Runnable> getExecutorType();
}