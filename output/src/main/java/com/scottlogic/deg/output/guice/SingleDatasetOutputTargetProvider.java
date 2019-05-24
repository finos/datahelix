package com.scottlogic.deg.output.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.scottlogic.deg.output.outputtarget.*;

import java.nio.file.Path;

public class SingleDatasetOutputTargetProvider implements Provider<SingleDatasetOutputTarget> {
    private final FileOutputTarget fileOutputTarget;
    private final TraceFileOutputTarget traceFileOutputTarget;
    private final StdoutOutputTarget stdoutOutputTarget;
    private final Path outputPath;
    private final boolean tracingIsEnabled;

    @Inject
    SingleDatasetOutputTargetProvider(
        FileOutputTarget fileOutputTarget,
        TraceFileOutputTarget traceFileOutputTarget,
        StdoutOutputTarget stdoutOutputTarget,
        @Named("config:outputPath")Path outputPath,
        @Named("config:tracingIsEnabled") boolean tracingIsEnabled) {
        this.fileOutputTarget = fileOutputTarget;
        this.traceFileOutputTarget = traceFileOutputTarget;
        this.stdoutOutputTarget = stdoutOutputTarget;
        this.outputPath = outputPath;
        this.tracingIsEnabled = tracingIsEnabled;
    }

    @Override
    public SingleDatasetOutputTarget get() {
        if (standardOut()){
            return stdoutOutputTarget;
        }

        if (tracingIsEnabled) {
            return new SplittingOutputTarget(fileOutputTarget, traceFileOutputTarget);
        } else {
            return fileOutputTarget;
        }
    }

    private boolean standardOut() {
        return outputPath == null;
    }
}
