package com.scottlogic.deg.generator.commandline;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.scottlogic.deg.generator.ConfigSource;
import com.scottlogic.deg.generator.guice.BaseModule;
import picocli.CommandLine;

import java.io.File;

/**
 * Abstract class to provide a generic run implementation for command line classes.
 */
public abstract class CommandLineBase implements Runnable, ConfigSource {

    @CommandLine.Parameters(index = "0", description = "The path of the profile json file.")
    File profileFile;

    @CommandLine.Option(
        names = {"--no-optimise"},
        description = "Prevents tree optimisation",
        hidden = true)
    boolean dontOptimise;

    @CommandLine.Option(
        names = "--help",
        usageHelp = true,
        description = "Display these available command line options")
    boolean help;

    @CommandLine.Option(
        names = {"--replace"},
        description = "Defines whether to overwrite/replace existing output files")
    boolean overwriteOutputFiles = false;

    @CommandLine.Option(
        names = { "--enable-schema-validation" },
        description = "Enables schema validation")
    boolean enableSchemaValidation = false;

    @Override
    public void run() {
        BaseModule container = new BaseModule(this);
        Injector injector = Guice.createInjector(container);

        Runnable task = injector.getInstance(getExecutorType());

        task.run();
    }

    protected abstract Class<? extends Runnable> getExecutorType();
}
