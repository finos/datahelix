package com.scottlogic.deg.generator.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.generation.*;

import java.io.PrintWriter;

public class MonitorProvider implements Provider<ReductiveDataGeneratorMonitor>  {
    private GenerationConfigSource commandLine;
    private NoopDataGeneratorMonitor noopDataGeneratorMonitor;

    @Inject
    MonitorProvider(
        GenerationConfigSource commandLine,
        NoopDataGeneratorMonitor noopDataGeneratorMonitor) {

        this.commandLine = commandLine;
        this.noopDataGeneratorMonitor = noopDataGeneratorMonitor;
    }

    @Override
    public ReductiveDataGeneratorMonitor get() {
        switch (commandLine.getMonitorType()) {
            case VERBOSE:
                return new MessagePrintingDataGeneratorMonitor(
                    new PrintWriter(System.err, true));

            case QUIET:
                return this.noopDataGeneratorMonitor;

            default:
                return new VelocityMonitor(
                    new PrintWriter(System.err, true));
        }
    }
}
