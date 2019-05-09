package com.scottlogic.deg.generator.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.generation.*;

public class MonitorProvider implements Provider<ReductiveDataGeneratorMonitor>  {

    private GenerationConfigSource commandLine;
    private VelocityMonitor velocityMonitor;
    private NoopDataGeneratorMonitor noopDataGeneratorMonitor;
    private SystemOutDataGeneratorMonitor systemOutDataGeneratorMonitor;

    @Inject
    MonitorProvider(GenerationConfigSource commandLine,
                    VelocityMonitor velocityMonitor,
                    NoopDataGeneratorMonitor noopDataGeneratorMonitor,
                    SystemOutDataGeneratorMonitor systemOutDataGeneratorMonitor) {
        this.commandLine = commandLine;
        this.velocityMonitor = velocityMonitor;
        this.noopDataGeneratorMonitor = noopDataGeneratorMonitor;
        this.systemOutDataGeneratorMonitor = systemOutDataGeneratorMonitor;
    }

    @Override
    public ReductiveDataGeneratorMonitor get() {
        if (commandLine.getOutputDestination().getDestination().equals("STREAM")) {
            return this.noopDataGeneratorMonitor;
        }
        switch (commandLine.getMonitorType()) {
            case VERBOSE:
                return this.systemOutDataGeneratorMonitor;

            case QUIET:
                return this.noopDataGeneratorMonitor;

            default:
                return this.velocityMonitor;
        }
    }
}
