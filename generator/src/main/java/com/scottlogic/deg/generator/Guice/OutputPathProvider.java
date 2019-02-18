package com.scottlogic.deg.generator.Guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.ConfigSource;

import java.nio.file.Path;

public class OutputPathProvider implements Provider<Path> {
    final ConfigSource configSource;

    @Inject
    public OutputPathProvider(ConfigSource configSource) {
        this.configSource = configSource;
    }

    @Override
    public Path get() {
        return configSource.getOutputPath();
    }
}
