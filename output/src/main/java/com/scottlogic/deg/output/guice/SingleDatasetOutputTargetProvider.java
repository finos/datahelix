package com.scottlogic.deg.output.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.scottlogic.deg.output.outputtarget.FileOutputTarget;
import com.scottlogic.deg.output.outputtarget.SingleDatasetOutputTarget;
import com.scottlogic.deg.output.outputtarget.StdoutOutputTarget;

import java.nio.file.Path;
import com.scottlogic.deg.output.outputtarget.SplittingOutputTarget;
import com.scottlogic.deg.output.outputtarget.TraceFileOutputTarget;

public class SingleDatasetOutputTargetProvider implements Provider<SingleDatasetOutputTarget> {
    private final OutputConfigSource outputConfigSource;
    private final FileOutputTarget fileOutputTarget;
    private final StdoutOutputTarget stdoutOutputTarget;

    private final TraceFileOutputTarget traceFileOutputTarget;
    private final boolean tracingIsEnabled;

    @Inject
    SingleDatasetOutputTargetProvider(
        OutputConfigSource outputConfigSource,
        FileOutputTarget fileOutputTarget,
        StdoutOutputTarget stdoutOutputTarget,
        TraceFileOutputTarget traceFileOutputTarget,
        @Named("config:tracingIsEnabled") boolean tracingIsEnabled) {
        this.outputConfigSource = outputConfigSource;
        this.fileOutputTarget = fileOutputTarget;
        this.stdoutOutputTarget = stdoutOutputTarget;
        this.traceFileOutputTarget = traceFileOutputTarget;
        this.tracingIsEnabled = tracingIsEnabled;
    }

    @Override
    public SingleDatasetOutputTarget get() {
        SingleDatasetOutputTarget target = outputConfigSource.useStdOut() ? stdoutOutputTarget : fileOutputTarget;
        return tracing(target);
    }

    public SingleDatasetOutputTarget tracing(SingleDatasetOutputTarget target) {
        return tracingIsEnabled ? new SplittingOutputTarget(target, traceFileOutputTarget) : target;
    }
}
