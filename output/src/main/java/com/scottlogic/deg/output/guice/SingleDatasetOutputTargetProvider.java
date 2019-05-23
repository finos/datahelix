package com.scottlogic.deg.output.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.scottlogic.deg.output.outputtarget.FileOutputTarget;
import com.scottlogic.deg.output.outputtarget.SingleDatasetOutputTarget;
import com.scottlogic.deg.output.outputtarget.SplittingOutputTarget;
import com.scottlogic.deg.output.outputtarget.TraceFileOutputTarget;

public class SingleDatasetOutputTargetProvider implements Provider<SingleDatasetOutputTarget> {
    private final FileOutputTarget fileOutputTarget;
    private final TraceFileOutputTarget traceFileOutputTarget;
    private final boolean tracingIsEnabled;

    @Inject
    SingleDatasetOutputTargetProvider(
        FileOutputTarget fileOutputTarget,
        TraceFileOutputTarget traceFileOutputTarget,
        @Named("config:tracingIsEnabled") boolean tracingIsEnabled) {
        this.fileOutputTarget = fileOutputTarget;
        this.traceFileOutputTarget = traceFileOutputTarget;
        this.tracingIsEnabled = tracingIsEnabled;
    }

    @Override
    public SingleDatasetOutputTarget get() {
        if (tracingIsEnabled) {
            return new SplittingOutputTarget(fileOutputTarget, traceFileOutputTarget);
        } else {
            return fileOutputTarget;
        }
    }
}
