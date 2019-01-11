package com.scottlogic.deg.generator.CommandLine;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.scottlogic.deg.generator.Guice.IoCContainer;

public abstract class CommandLineBase implements Runnable {
    @Override
    public void run() {
        IoCContainer container = new IoCContainer(this);
        Injector injector = Guice.createInjector(container);

        Runnable task = injector.getInstance(getExecutorType());

        task.run();
    }

    protected abstract Class<? extends Runnable> getExecutorType();
}
