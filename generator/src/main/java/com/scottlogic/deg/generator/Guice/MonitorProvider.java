package com.scottlogic.deg.generator.Guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.CommandLine.GenerateCommandLine;
import com.scottlogic.deg.generator.generation.DataGeneratorMonitor;
import com.scottlogic.deg.generator.generation.NoopDataGeneratorMonitor;
import com.scottlogic.deg.generator.generation.ReductiveDataGeneratorMonitor;
import com.scottlogic.deg.generator.generation.VelocityMonitor;

public class MonitorProvider implements Provider<ReductiveDataGeneratorMonitor> {

    private GenerateCommandLine commandLine;

    @Inject
    MonitorProvider(GenerateCommandLine commandLine) {
        this.commandLine = commandLine;
    }

    @Override
    public ReductiveDataGeneratorMonitor get() {
        switch (commandLine.getMonitorType()){
            case NOOP:
                return new NoopDataGeneratorMonitor();

            case VELOCITY:
                return new VelocityMonitor();

            default:
                return new VelocityMonitor();
        }
    }
}
